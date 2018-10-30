package com.qixiu.newoulingzhu.mvp.view.activity.base.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.ImgHelper;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.ImageCaptureManager;
import okhttp3.Call;

/**
 * Created by gyh on 2017/6/30 0030.
 * TODO 注意！由于上传图片采用Base64方式，那么压缩图片到byte64的字节量很大，一定要放到异步里面去做，而后面的后网络请求也放到异步里面
 * TODO，从而回调就需要在主线程里面做了。
 */

public abstract class UploadPictureActivity extends TitleActivity implements OnRecyclerItemListener, OKHttpUIUpdataListener, AsyncTaskFactory.AsyncTaskInterface<Object, Object, String> {

    private ImageCaptureManager captureManager;
    private UpLoadPictureAdapter mRcAdapter;
    private int maxPictureCount = 9;
    protected ArrayList<String> selectPhotos = new ArrayList<>();
    private RecyclerView mRecyclerView;


    protected OKHttpRequestModel mOkHttpRequestModel;
    protected ZProgressHUD mZProgressHUD;
    private Map<String, String> mMap;
    private String mUrl;
    private String imagkey;
    private BaseBean baseBean;


    protected boolean isSelected() {
        return selectPhotos.size() > 0;
    }

    protected void setItemLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (mRcAdapter != null) {
            mRcAdapter.setItemLayoutParams(layoutParams);
        }

    }

    /**
     * 作为该类的子类该方法就不要重写了，请在initUpLoadView来做初始化view的操作
     */
    @Override
    protected void onInitView() {
        super.onInitView();
        mZProgressHUD = new ZProgressHUD(this);
        mZProgressHUD.setMessage("发布中...");
        mRcAdapter = new UpLoadPictureAdapter();
        mRcAdapter.setOnItemClickListener(this);
        initUpLoadView();
        setRecyclerView(getRecyclerView());
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        captureManager = new ImageCaptureManager(this);
    }


    protected void requestUpLoad(String url, String imagkey, Map<String, String> map) {
        this.imagkey = imagkey;
        this.mUrl = url;

        if (map == null) {

            mMap = new HashMap();
        } else {
            mMap = map;
        }
        String uid = Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING);
        if (!TextUtils.isEmpty(uid)) {
            mMap.put(IntentDataKeyConstant.UID_KEY, uid);
        }

        AsyncTask asyncTask = AsyncTaskFactory.CreateDefaultAsyncTask(this);
        asyncTask.execute();


    }

    protected void requestUpLoad(String url, String imagkey, Map<String, String> map, BaseBean baseBean) {
        this.baseBean = baseBean;
        requestUpLoad(url, imagkey, map);
    }

    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        mRecyclerView = getRecyclerView();
        if (mRecyclerView == null) return;
        mRecyclerView.setAdapter(mRcAdapter);
    }

    protected void setMaxPictureCount(int maxPictureCount) {
        if (maxPictureCount <= 0) {
            this.maxPictureCount = 9;
            if (mRcAdapter != null) {
                mRcAdapter.setMaxPictureCount(9);
            }

        } else {
            this.maxPictureCount = maxPictureCount;
            if (mRcAdapter != null) {
                mRcAdapter.setMaxPictureCount(maxPictureCount);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) {
                //获取图片选择器的图片路径们
                if (data != null) {

                    selectPhotos.clear();
//                    selectedBitmap.clear();
                    List<String> photos =
                            data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                   /* if (photos != null && photos.size() > 0)
                    {

                        for (int i = 0; i < photos.size() ; i++)
                        {
                            Bitmap bitmap = revolvePicture(photos.get(i));
                            selectedBitmap.add(bitmap);
                        }*/
                    selectPhotos.addAll(photos);
                    /*}*/
                }
            } else if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO) {
                captureManager.galleryAddPic();
                String photoPath = captureManager.getCurrentPhotoPath();

                selectPhotos.remove(StringConstants.EMPTY_STRING);
                //@author gyh 限制如果达到最大张数拍照后的图片就不添加
                if (selectPhotos.size() < maxPictureCount) {
                    selectPhotos.add(photoPath);
                    //   selectedBitmap.add(revolvePicture(photoPath));//将拍照获得照片转成Bitmap。添加到集合中
                } else {
                    ToastUtil.toast("已经添加了" + maxPictureCount + "张图片");
                }

            }

            mRcAdapter.refreshData(selectPhotos);

        }
    }


    protected abstract void onUpLoad(Object data);

    public abstract void initUpLoadView();

    public abstract RecyclerView getRecyclerView();

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        int position = mRecyclerView.getChildLayoutPosition(v);
        if (position == mRcAdapter.getDatas().size()) {
            if (selectPhotos.size() < maxPictureCount) {
                PhotoPicker.builder().setPhotoCount(maxPictureCount).setShowCamera(true).setSelected(
                        selectPhotos).start(this);
            }
        } else {
            PhotoPreview.builder().setPhotos(selectPhotos).setCurrentItem(position).start(
                    this);
        }
    }

    @Override
    public void onSuccess(final Object data, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mZProgressHUD.isShowing()) {
                    mZProgressHUD.dismissWithSuccess("发布成功");
                }
                /**
                 * TODO 注意！这里由于dismissWithSuccess内部是利用AsyncTask 设置消息后延迟500毫秒才关闭掉，直接finish会异常
                 */
                AsyncTask<String, Integer, Long> task = new AsyncTask<String, Integer, Long>() {

                    @Override
                    protected Long doInBackground(String... params) {
                        SystemClock.sleep(501);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Long result) {
                        super.onPostExecute(result);
                        String simpleName = getClass().getSimpleName();
                        onUpLoad(data);


                    }
                };
                task.execute();
            }
        });
    }

    @Override
    public void onFailure(final C_CodeBean c_codeBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mZProgressHUD.isShowing()) {
                    mZProgressHUD.dismiss();
                    ToastUtil.toast(c_codeBean.getM());
                }

            }
        });
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mZProgressHUD.isShowing()) {
                    mZProgressHUD.dismiss();
                    ToastUtil.toast(getString(R.string.link_error));
                }
            }
        });
    }

    @Override
    public String doInBackground(Object... params) {
        StringBuffer images = new StringBuffer();
        for (int i = 0; i < selectPhotos.size(); i++) {
            try {
                Bitmap bitmap = ImgHelper.revolvePicture(selectPhotos.get(i));

                if (bitmap != null) {
                    images.append(ImgHelper.bitmap2StrByBase64(bitmap));
                    if (i < selectPhotos.size() - 1) {
                        images.append("|");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String s = images.toString();
        if (!TextUtils.isEmpty(s)) {
            if (!TextUtils.isEmpty(imagkey)) {
                mMap.put(imagkey, s);
            } else {
                mMap.put(StringConstants.IMGS_STRING, s);
            }

        }
        if (baseBean == null) {
            baseBean = new BaseBean();
        }
        mOkHttpRequestModel.okhHttpPost(mUrl, mMap, baseBean);
        return s;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(String s) {
        mMap.clear();
        mMap = null;
    }

    @Override
    public void onProgressUpdate(Object... values) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRcAdapter.refreshData(selectPhotos);
    }
}
