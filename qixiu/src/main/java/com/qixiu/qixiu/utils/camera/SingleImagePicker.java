package com.qixiu.qixiu.utils.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.qixiu.qixiu.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Long on 2017/11/24
 * 选择图片(单张)
 * <br/>
 * 依赖(com.yalantis:ucrop:1.5.0 && pub.devrel:easypermissions:0.3.0)
 * <br/>
 * 需要在Manifest中声明UCropActivity Like:
 * <br/>
 * <pre class="prettyprint">
 * &lt;activity
 * android:name="com.yalantis.ucrop.UCropActivity"
 * android:screenOrientation="portrait"
 * android:theme="@style/Theme.AppCompat.Light.NoActionBar"/&gt;
 * </pre>
 */
public final class SingleImagePicker {

    private static final String TAG = "SingleImagePicker";

    private static final String PERM_CAMERA = Manifest.permission.CAMERA;
    private static final String PERM_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /*存储权限 requestCode*/
    private static final int RC_PERM_STORAGE = 254;
    /*相机权限 requestCode*/
    private static final int RC_PERM_CAMERA  = 255;
    /*APP设置界面 requestCode*/
    private static final int RC_APP_SETTING  = 256;

    /*相机拍照 requestCode*/
    private static final int RC_CAPTURE    = 0xFA;
    /*相册选取 requestCode*/
    private static final int RC_PICK       = 0xFB;
    /*裁剪图片 requestCode*/
    private static final int RC_IMAGE_CROP = 0xFC;

    // host activity
    private WeakReference<Activity> mHostAct;

    //选择方式(0-->拍照，1-->相册)
    private int mPickWay = -1;

    //临时文件
    private List<String> mTempFiles = new ArrayList<>();

    //裁剪图片配置选项
    private UCrop.Options mOptions;
    //是否需要裁剪,默认裁剪
    private boolean isNeedCrop = true;

    //回调
    private Callback mCallback;

    public interface Callback {

        void onSuccess(Uri uri);
    }

    /*在Activity中使用*/
    public SingleImagePicker(@NonNull Activity activity) {
        this(activity, true);
    }

    public SingleImagePicker(@NonNull Activity activity, boolean isNeedCrop) {
        mHostAct = new WeakReference<>(activity);
        this.isNeedCrop = isNeedCrop;
    }

    /**
     * 设置图片裁剪配置选项
     *
     * @param options 裁剪配置
     */
    public void setOptions(@NonNull UCrop.Options options) {
        mOptions = options;
    }

    public void setCallback(@NonNull Callback callback) {
        mCallback = callback;
    }

    /**
     * 开始选择图片，弹出选择方式对话框
     */
    public void onStart() {
        showImagePickDialog(R.string.title_image_picker);
    }

    public void onStart(@StringRes int titleRes) {
        showImagePickDialog(titleRes);
    }


    /**
     * 弹出选择图片方式
     *
     * @param strRes 弹窗标题
     */
    private void showImagePickDialog(@StringRes int strRes) {
        if (mHostAct.get() == null) return;
        final String[] items = getContext().getResources().getStringArray(R.array.image_pick_ways);
        new AlertDialog.Builder(getContext())
                .setTitle(getContext().getString(strRes))
                .setItems(items, (dialog, which) -> {
                    mPickWay = which;
                    onStartPick();
                }).show();
    }

    /**
     * 开始选择图片
     */
    private void onStartPick() {
        if (mPickWay == 0) {
            //拍照
            startCapture();
        } else {
            //相册
            startPick();
        }
    }

    /**
     * 调用相机拍照
     */
    public void startCapture() {
        if (EasyPermissions.hasPermissions(getContext(), PERM_CAMERA)) {
            startCaptureInternal();
        } else {
            if (mHostAct.get() != null) {
                EasyPermissions.requestPermissions(mHostAct.get(),
                        getContext().getString(R.string.rationale_image_pick_camera),
                        RC_PERM_CAMERA, PERM_CAMERA);
            }
        }
    }

    /**
     * 从相册选取图片
     */
    public void startPick() {
        if (EasyPermissions.hasPermissions(getContext(), PERM_STORAGE)) {
            startPickInternal();
        } else {
            if (mHostAct.get() != null) {
                EasyPermissions.requestPermissions(mHostAct.get(),
                        getContext().getString(R.string.rationale_image_pick_storage),
                        RC_PERM_STORAGE, PERM_STORAGE);
            }
        }
    }


    /**
     * 清除拍照或裁剪保存的图片
     */
    public void clear() {
        mHostAct.clear();
        mCallback = null;
        //删除保存的图片
        if (mTempFiles.size() > 0) {
            for (String path : mTempFiles) {
                File temp = new File(path);
                if (temp.exists()) {
                    boolean delete = temp.delete();
                    com.qixiu.qixiu.utils.camera.Logger.d(TAG, "删除拍照保存的图片----" + path + "------" + delete);
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.qixiu.qixiu.utils.camera. Logger.d(TAG, "onActivityResult: " + resultCode + "-----" + requestCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK:
                //相册
                Uri picturePath = data.getData();
                com.qixiu.qixiu.utils.camera.Logger.d(TAG, "onActivityResult: " + picturePath);
                startCrop(picturePath);
                break;
            case RC_CAPTURE:
                //拍照
                Uri captureUri = Uri.fromFile(new File(mTempFiles.get(mTempFiles.size() - 1)));
                com.qixiu.qixiu.utils.camera. Logger.d(TAG, "onActivityResult: " + captureUri);
                startCrop(captureUri);
                break;
            case RC_IMAGE_CROP:
                //裁剪
                if (data != null) {
                    Uri output = UCrop.getOutput(data);
                    if (mCallback != null) {
                        mCallback.onSuccess(output);
                    }
                }
                break;
            case RC_APP_SETTING:
                //用户设置了权限返回此页面
                //startCapture();
                //ignore
                break;
        }
    }

    public void onPermissionsGranted(int requestCode, List<String> perms) {
        com.qixiu.qixiu.utils.camera.    Logger.d(TAG, "onPermissionsGranted: " + requestCode);
        if (requestCode == RC_PERM_CAMERA) {
            startCaptureInternal();
        } else if (requestCode == RC_PERM_STORAGE) {
            startPickInternal();
        }
    }

    public void onPermissionsDenied(int requestCode, List<String> perms) {
        com.qixiu.qixiu.utils.camera.   Logger.d(TAG, "onPermissionsDenied: " + requestCode);
        if (EasyPermissions.somePermissionPermanentlyDenied(mHostAct.get(), perms)) {
            showRationaleDialog(requestCode);
        }
    }

    /*永久拒绝后显示说明*/
    private void showRationaleDialog(int requestCode) {
        String rationale;
        if (requestCode == RC_PERM_STORAGE) {
            rationale = getContext().getString(R.string.rationale_app_setting,
                    getContext().getString(R.string.perm_storage));
        } else {
            rationale = getContext().getString(R.string.rationale_app_setting,
                    getContext().getString(R.string.perm_camera));
        }
        new AppSettingsDialog.Builder(mHostAct.get())
                .setTitle(R.string.title_image_pick_setting)
                .setRationale(rationale)
                .setPositiveButton(R.string.positive_setting)
                .setRequestCode(RC_APP_SETTING)
                .build().show();
    }


    /**
     * 调起系统相机
     */
    private void startCaptureInternal() {
        File temp = FileSystem.createImageTempFile(getContext());
        mTempFiles.add(temp.getAbsolutePath());
        IntentUtils.startCapture(mHostAct.get(), RC_CAPTURE, temp);
    }

    /**
     * 调用图片选择
     */
    private void startPickInternal() {
        IntentUtils.startActionPick(mHostAct.get(), RC_PICK);
    }

    /**
     * 裁剪
     *
     * @param source 要裁剪的图片uri
     */
    private void startCrop(Uri source) {
        if (!isNeedCrop) {
            //不需要裁剪，直接返回
            if (mCallback != null) {
                mCallback.onSuccess(source);
                return;
            }
        }
        File temp = FileSystem.createImageTempFile(getContext());
        mTempFiles.add(temp.getAbsolutePath());
        UCrop uCrop = UCrop.of(source, Uri.fromFile(temp))
                .withAspectRatio(1, 1);
        if (mOptions != null) {
            uCrop.withOptions(mOptions);
        }
        if (mHostAct.get() != null) {
            uCrop.start(mHostAct.get(), RC_IMAGE_CROP);
        }
    }

    private Context getContext() {
        return mHostAct.get();
    }

}
