package com.qixiu.newoulingzhu.mvp.view.activity.base.upload;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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
import com.qixiu.qixiu.utils.PictureCut;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;

import java.io.File;
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

public abstract class UploadPictureActivityNew extends TitleActivity implements OnRecyclerItemListener, OKHttpUIUpdataListener<BaseBean>, AsyncTaskFactory.AsyncTaskInterface<Object, Object, String> {
    private ImageCaptureManager captureManager;
    public UpLoadPictureAdapter mRcAdapter;
    private int maxPictureCount = 9;
    protected ArrayList<String> selectPhotos = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean is_finish = true;//判断上传完毕后是否退出界面
    private boolean is_preview_can_delete = true;
    private OKHttpRequestModel mOkHttpRequestModel;
    protected ZProgressHUD mZProgressHUD;
    private Map<String, String> mMap;
    private String mUrl;
    private BaseBean uploadbean;
    private String imageKey;//上传图片的字段
    private String connectionSymbol;//上传图片的连接符号

    public void setIs_preview_can_delete(boolean is_preview_can_delete) {
        this.is_preview_can_delete = is_preview_can_delete;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }

    public void setConnectionSymbol(String connectionSymbol) {
        this.connectionSymbol = connectionSymbol;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }


    public void setUploadbean(BaseBean uploadbean) {
        this.uploadbean = uploadbean;
    }

    /**
     * 作为该类的子类该方法就不要重写了，请在initUpLoadView来做初始化view的操作
     */
    @Override
    protected void onInitView() {
        super.onInitView();
        mZProgressHUD = new ZProgressHUD(this);
        mZProgressHUD.setMessage("上传中...");
        initUpLoadView();
        mRecyclerView = getRecyclerView();
        if (mRecyclerView == null) return;
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        captureManager = new ImageCaptureManager(this);
        mRcAdapter = new UpLoadPictureAdapter();
        mRcAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mRcAdapter);
    }


    protected void requestUpLoad(String url, Map<String, String> map) {
        mZProgressHUD.show();
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
        final StringBuffer images = new StringBuffer();
        try {
            //第一套压缩方案
//            PictureCut.CompressImageWithThumb.callBase64s(selectPhotos, new PictureCut.CompressImageWithThumb.CallBackBase64s() {
//                @Override
//                public void callBack(List<String> base64s) {
//                    for (int i = 0; i < base64s.size(); i++) {
//                        images.append(base64s.get(i));
//                        if (i < selectPhotos.size() - 1) {
//                            images.append((TextUtils.isEmpty(connectionSymbol)) ? "|" : connectionSymbol);
//                        }
//                    }
//                    String s = images.toString();
//                    if (!TextUtils.isEmpty(s)) {
//                        mMap.put(TextUtils.isEmpty(imageKey) ? StringConstants.IMGS_STRING : imageKey, s);
//                    }
//                    AsyncTask asyncTask = AsyncTaskFactory.CreateDefaultAsyncTask(UploadPictureActivityNew.this);
//                    asyncTask.execute();
//                }
//            });
            //第二套压缩方案
//            PictureCut.CompressRxjava.compress(getContext(), selectPhotos, new PictureCut.CallBack<List<String>>() {
//                @Override
//                public void call(List<String> base64s) {
//                    for (int i = 0; i < base64s.size(); i++) {
//                        images.append(base64s.get(i));
//                        if (i < selectPhotos.size() - 1) {
//                            images.append((TextUtils.isEmpty(connectionSymbol))?"|":connectionSymbol);
//                        }
//                    }
//                    String s = images.toString();
//                    if (!TextUtils.isEmpty(s)) {
//                        mMap.put(TextUtils.isEmpty(imageKey)? StringConstants.IMGS_STRING:imageKey, s);
//                    }
//                    AsyncTask asyncTask = AsyncTaskFactory.CreateDefaultAsyncTask(UploadPictureActivityNew.this);
//                    asyncTask.execute();
//                }
//            });
            //第三套压缩方案
            PictureCut.CompressLuban.comPress(getContext(), selectPhotos, new PictureCut.CallBack<List<File>>() {
                @Override
                public void call(List<File> files) {
                    PictureCut.CompressLuban.toBase64s(files, new PictureCut.CallBack<List<String>>() {
                        @Override
                        public void call(List<String> base64s) {
                            for (int i = 0; i < base64s.size(); i++) {
                                images.append(base64s.get(i));
                                if (i < selectPhotos.size() - 1) {
                                    images.append((TextUtils.isEmpty(connectionSymbol)) ? "|" : connectionSymbol);
                                }
                            }
                            String s = images.toString();
                            if (!TextUtils.isEmpty(s)) {
                                mMap.put(TextUtils.isEmpty(imageKey) ? StringConstants.IMGS_STRING : imageKey, s);
                            }
                            AsyncTask asyncTask = AsyncTaskFactory.CreateDefaultAsyncTask(UploadPictureActivityNew.this);
                            asyncTask.execute();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AsyncTask asyncTask = AsyncTaskFactory.CreateDefaultAsyncTask(UploadPictureActivityNew.this);
            asyncTask.execute();
        }


    }

    protected void setMaxPictureCount(int maxPictureCount) {
        if (maxPictureCount <= 0) {
            this.maxPictureCount = 9;
        } else {
            this.maxPictureCount = maxPictureCount;
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
            PhotoPreview.builder().setPhotos(selectPhotos).setShowDeleteButton(is_preview_can_delete).setCurrentItem(position).start(
                    this);
        }
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        onUpLoad(data);
        if (mUrl != null && mUrl.equals(data.getUrl())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mZProgressHUD.isShowing()) {
                        mZProgressHUD.dismissWithSuccess("上传成功");
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
                            if (is_finish) {
                                finish();
                            }
                        }
                    };
                    task.execute();
                }
            });
        }
    }

    @Override
    public void onFailure(final C_CodeBean c_codeBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mZProgressHUD.isShowing()) {
                    mZProgressHUD.dismissWithFailure("上传失败");
                }
                ToastUtil.toast(c_codeBean.getM());
            }
        });
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mZProgressHUD.isShowing()) {
                    mZProgressHUD.dismissWithFailure("上传失败");
                }
            }
        });
    }

    @Override
    public String doInBackground(Object... params) {
        mOkHttpRequestModel.okhHttpPost(mUrl, mMap, uploadbean == null ? new BaseBean() : uploadbean );
        return "";
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(String s) {
//        if (!TextUtils.isEmpty(s)) {
//            mMap.put(StringConstants.IMGS_STRING, s);
//        }
//        mOkHttpRequestModel.okhHttpPost(mUrl, mMap, new BaseBean(), false);
        mMap.clear();
        mMap = null;
    }

    @Override
    public void onProgressUpdate(Object... values) {

    }
}
