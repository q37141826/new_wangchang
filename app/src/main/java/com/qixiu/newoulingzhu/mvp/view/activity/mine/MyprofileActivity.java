package com.qixiu.newoulingzhu.mvp.view.activity.mine;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.adapter.GlideCacheUtil;
import com.hyphenate.easeui.bean.StringConstants;
import com.qixiu.newoulingzhu.beans.mine_bean.MyProfileBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.edit.BindPhoneActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.edit.EditSingleProfileActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.edit.ResetPhoneActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.MyFileProvider;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.FileProviderUtil;
import com.qixiu.qixiu.utils.PhotoBitmapUtils;
import com.qixiu.qixiu.utils.PictureCut;
import com.qixiu.qixiu.utils.camera.SingleImagePicker;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.IntelligentTextWatcher;
import com.qixiu.wigit.myedittext.TextWatcherAdapterInterface;
import com.qixiu.wigit.picker.AddressInitTask;
import com.qixiu.wigit.picker.MyPopOneListPicker;
import com.qixiu.wigit.picker.SelectedDataBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.utils.ImageCaptureManager;
import okhttp3.Call;

public class MyprofileActivity extends TitleActivity implements View.OnClickListener, TextWatcherAdapterInterface, OKHttpUIUpdataListener<BaseBean>, AddressInitTask.PickListene {
    @BindView(R.id.imageView_test)
    ImageView imageViewTest;
    private RelativeLayout relativeLayout_changehead, relativeLayout_changenickname, relativeLayout_changesex, relativeLayout_changepsw;
    private TextView textView_nickname_change, textView_sex_change;
    private PopupWindow popwindow;
    private Button btn_confirm_edituser;
    private MyPopForInPutAlterStyle pop;
    private MyPopOneListPicker sexPicker;
    //图片选择器参数
    ArrayList<String> selectPhotos = new ArrayList<>();
    int MAX_IMAGE = 1;
    private ImageCaptureManager captureManager;
    //图片路径

    String photoPath;
    private static final String EMPTY_PATH = StringConstants.EMPTY_STRING;
    private CircleImageView circular_head_edit;
    //照相机权限
    String permissions[] = {Manifest.permission.CAMERA};
    private IntelligentTextWatcher mIntelligentTextWatcher;
    ZProgressHUD zProgressHUD;
    //标题右边的权限
    TextView textTitleRight;
    //性别
    String sex = "2";
    String nickname = "";
    private SingleImagePicker mImagePicker;


    //照相机路径，照相机和相册的返回码
    String FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
    private final int CANMERA = 10001, ABLUM = 10002;
    private TextView mTextView_address_change;
    private String province;
    private String city;
    private String town;

    private View mRelativeLayout_address_mypro;
    private OKHttpRequestModel mOkHttpRequestModel=new OKHttpRequestModel(this);
    private StringBuffer mStringBuffer;
    private TextView mTextView_truename_change;
    private TextView mTextView_phone_change;
    private TextView mTextView_mailAdress_change;
    private RelativeLayout mRelativeLayout_changeTrueName;
    private View mRelativeLayout_mailAdrees_mypro;
    private View mRelativeLayout_phone_mypro;
    private Uri imageUri;
    private File photofile;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyprofileActivity.class));
    }


    @Override
    protected void onInitData() {


        photofile = new File(FILE_PATH, "tongdi.jpg");

        String s = Preference.get(ConstantString.HEAD, "");
        Glide.with(this).load(s).error(R.mipmap.headplace).into(circular_head_edit);


        if (Preference.get(ConstantString.SEX, "").equals(StringConstants.STRING_2)) {
            textView_sex_change.setText("女");
        } else {
            textView_sex_change.setText("男");
        }
        mTextView_address_change.setText(TextUtils.isEmpty(Preference.get(ConstantString.ADDRESS, StringConstants.EMPTY_STRING)) ? "未填写" : Preference.get(ConstantString.ADDRESS, StringConstants.EMPTY_STRING));
        mTextView_phone_change.setText(Preference.get(ConstantString.PHONE, StringConstants.EMPTY_STRING));
        textView_nickname_change.setText(Preference.get(ConstantString.NICK_NAME, "").equals("") ? Preference.get(ConstantString.PHONE, "") : Preference.get(ConstantString.NICK_NAME, ""));
        mTextView_truename_change.setText(TextUtils.isEmpty(Preference.get(ConstantString.TRUE_NAME, StringConstants.EMPTY_STRING)) ? "未填写" : Preference.get(ConstantString.TRUE_NAME, StringConstants.EMPTY_STRING));
        mTextView_mailAdress_change.setText(TextUtils.isEmpty(Preference.get(ConstantString.EMAIL, StringConstants.EMPTY_STRING)) ? "未填写" : Preference.get(ConstantString.EMAIL, StringConstants.EMPTY_STRING));
    }

    private void requstDetailsData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.personDetailsUrl,map,new MyProfileBean());
    }

    @Override
    protected void onInitViewNew() {
        zProgressHUD = new ZProgressHUD(this);
        captureManager = new ImageCaptureManager(this);
        relativeLayout_changepsw = (RelativeLayout) findViewById(R.id.relativeLayout_changepsw);
        textView_sex_change = (TextView) findViewById(R.id.textView_sex_change);
        relativeLayout_changesex = (RelativeLayout) findViewById(R.id.relativeLayout_changesex);
        relativeLayout_changehead = (RelativeLayout) findViewById(R.id.relativeLayout_changehead);
        relativeLayout_changenickname = (RelativeLayout) findViewById(R.id.relativeLayout_changenickname);
        mRelativeLayout_changeTrueName = (RelativeLayout) findViewById(R.id.relativeLayout_changeTrueName);
        mRelativeLayout_mailAdrees_mypro = findViewById(R.id.relativeLayout_mailAdrees_mypro);
        mRelativeLayout_phone_mypro = findViewById(R.id.relativeLayout_phone_mypro);
        circular_head_edit = (CircleImageView) findViewById(R.id.circular_head_edit);
        btn_confirm_edituser = (Button) findViewById(R.id.btn_confirm_edituser);
        textView_nickname_change = (TextView) findViewById(R.id.textView_nickname_change);
        mTextView_truename_change = (TextView) findViewById(R.id.textView_truename_change);
        mTextView_phone_change = (TextView) findViewById(R.id.textView_phone_change);
        mTextView_mailAdress_change = (TextView) findViewById(R.id.textView_mailAdress_change);
        mRelativeLayout_address_mypro = findViewById(R.id.relativeLayout_address_mypro);
        mTextView_address_change = (TextView) findViewById(R.id.textView_address_change);

        initTitle();
        setClick();
    }

    private void setClick() {
        relativeLayout_changehead.setOnClickListener(this);
        relativeLayout_changenickname.setOnClickListener(this);
        relativeLayout_changesex.setOnClickListener(this);
        relativeLayout_changepsw.setOnClickListener(this);
        btn_confirm_edituser.setOnClickListener(this);
        mRelativeLayout_address_mypro.setOnClickListener(this);
        mRelativeLayout_changeTrueName.setOnClickListener(this);
        mRelativeLayout_mailAdrees_mypro.setOnClickListener(this);
        mRelativeLayout_phone_mypro.setOnClickListener(this);
    }

    private void initTitle() {
        mTitleView.setTitle("个人资料");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitleRight = (TextView) findViewById(mTitleView.getRightId());
    }

    private void startEditUser(String base64, String nickname) {
        if (textView_sex_change.getText().toString().equals("男")) {
            sex = 1 + "";
        } else if (textView_sex_change.getText().toString().equals("女")) {
            sex = 2 + "";
        } else {
            sex = 3 + "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("nickname", nickname);
        map.put("sex", sex);
        if (!base64.equals("")) {
            map.put("head", base64);
        }
        new OKHttpRequestModel<BaseBean>(this).okhHttpPost(ConstantUrl.editUserUrl, map, new BaseBean<BaseBean>(), true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_myprofile;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.relativeLayout_changehead:
                showEditPopwindow();
                break;
            case R.id.imageView_camera_camera:
                popwindow.dismiss();
//                gotoCamera();
                autoObtainCameraPermission();
//                if (mImagePicker == null) {
//                    mImagePicker = new SingleImagePicker(this);
//                    mImagePicker.setCallback(uri -> {
//                        String path = FileProviderUtil.getFilePath(this,uri);
//                        Log.e("path",path);
//                    });
//                }
//                mImagePicker.onStart();
                break;
            case R.id.imageView_camera_ablum:
                startPick();
                popwindow.dismiss();
                break;
            case R.id.relativeLayout_changenickname:
                Intent editNickName = new Intent(this, EditSingleProfileActivity.class);
                editNickName.putExtra(IntentDataKeyConstant.EDIT_TYPE, IntentDataKeyConstant.NICKNAME);
                editNickName.putExtra(IntentDataKeyConstant.NAME, Preference.get(ConstantString.NICK_NAME, ""));
                startActivityForResult(editNickName, IntentRequestCodeConstant.REQUESTCODE_EDITSINGLE);
                break;
            case R.id.relativeLayout_changeTrueName:
                Intent editTrueName = new Intent(this, EditSingleProfileActivity.class);
                editTrueName.putExtra(IntentDataKeyConstant.EDIT_TYPE, IntentDataKeyConstant.TRUENAME);
                editTrueName.putExtra(IntentDataKeyConstant.NAME, Preference.get(ConstantString.TRUE_NAME, ""));
                startActivityForResult(editTrueName, IntentRequestCodeConstant.REQUESTCODE_EDITSINGLE);
                break;
            case R.id.relativeLayout_phone_mypro:
                if (TextUtils.isEmpty(mTextView_phone_change.getText().toString())) {
                    BindPhoneActivity.start(getContext());
                } else {
                    Intent reseltPhone = new Intent(this, ResetPhoneActivity.class);
                    reseltPhone.setAction(IntentDataKeyConstant.ACTION_EDITPHONE_ORIGINAL);
                    reseltPhone.putExtra(IntentDataKeyConstant.NAME, Preference.get(ConstantString.PHONE, ""));
                    startActivityForResult(reseltPhone, IntentRequestCodeConstant.REQUESTCODE_RESETPHONE);
                }
                break;
            case R.id.relativeLayout_mailAdrees_mypro:
                Intent editEmailName = new Intent(this, EditSingleProfileActivity.class);
                editEmailName.putExtra(IntentDataKeyConstant.EDIT_TYPE, IntentDataKeyConstant.EMAIL);
                editEmailName.putExtra(IntentDataKeyConstant.NAME, Preference.get(ConstantString.EMAIL, ""));
                startActivityForResult(editEmailName, IntentRequestCodeConstant.REQUESTCODE_EDITSINGLE);
                break;
            case R.id.relativeLayout_changesex:
                if (sexPicker != null) {
                    sexPicker.show();
                } else {
                    List<SelectedDataBean> list = new ArrayList<>();
                    SelectedDataBean bean = new SelectedDataBean("0", "男");
                    list.add(bean);
                    bean = new SelectedDataBean("1", "女");
                    list.add(bean);
                    sexPicker = new MyPopOneListPicker(this, list, new MyPopOneListPicker.Pop_selectedListenner() {
                        @Override
                        public void getData(SelectedDataBean data) {
                            if ("女".equals(data.getText())) {
                                sex = StringConstants.STRING_2;
                            } else {
                                sex = StringConstants.STRING_1;
                            }
                            textView_sex_change.setText(data.getText());
                            requestEditSex(sex);
                        }
                    });

                }
                break;
            case R.id.relativeLayout_changepsw:
                intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra(ConstantString.TITLE_NAME, "修改密码");
                startActivity(intent);
                break;
            case R.id.btn_confirm_edituser:
                startCompress();
                break;
            case R.id.relativeLayout_address_mypro:

                AddressInitTask execute = new AddressInitTask(this);
                //todo被测试要求不准设置默认地址
                execute.execute("", "", "");
                execute.setOnAddressPickListene(this);
                break;
        }
    }

    private void gotoCamera() {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(FILE_PATH, "tongdi.jpg");
        photoPath = file.getPath();
        // 把文件地址转换成Uri格式
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = MyFileProvider.getUriForFile(this, getString(R.string.provider), file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CANMERA);
//
    }

    private void requestEditSex(String sex) {
        if (mOkHttpRequestModel == null) {
            mOkHttpRequestModel = new OKHttpRequestModel(this);
        }
        // zProgressHUD.show();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(ConstantString.SEX, sex);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.EditSexURl, stringMap, new BaseBean());
    }

    public void startPick() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        //此处调用了图片选择器
        //如果直接写intent.setDataAndType("image/*");
        //调用的是系统图库
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(Intent.createChooser(intent, "请选择一个文件"), ABLUM);
    }

    private void showEditPopwindow() {
        View view = View.inflate(this, R.layout.pop_camera_selector, null);
        try {
            popwindow = new PopupWindow(view);
            popwindow.setFocusable(true);
            popwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popwindow.setClippingEnabled(false);
            popwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popwindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
        }
        RelativeLayout relativelayout_camera_dismiss = (RelativeLayout) view.findViewById(R.id.relativelayout_camera_dismiss);
        relativelayout_camera_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
        ImageView imageView_camera_camera = (ImageView) view.findViewById(R.id.imageView_camera_camera);
        ImageView imageView_camera_ablum = (ImageView) view.findViewById(R.id.imageView_camera_ablum);
        imageView_camera_camera.setOnClickListener(this);
        imageView_camera_ablum.setOnClickListener(this);
    }

    private void startCompress() {
        //// TODO: 2017/7/19 保存修改资料的接口
        nickname = textView_nickname_change.getText().toString();
        if (nickname.equals("")) {
            ToastUtil.showToast(MyprofileActivity.this, "请输入合法昵称");
            return;
        }
        textTitleRight.setEnabled(false);
        zProgressHUD.show();
        if (selectPhotos.size() == 0) {
            startEditUser("", nickname);
        } else {
            PictureCut.CompressImageWithThumb.callBase64(selectPhotos.get(0), new PictureCut.CompressImageWithThumb.CallBackBase64() {
                @Override
                public void callBack(String base64) {
                    startEditUser(base64, nickname);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAMERA_REQUEST) {
            try {
                if (resultCode == RESULT_OK) {
                    zProgressHUD.show();
                    photoPath= PhotoBitmapUtils.amendRotatePhoto(photoPath, getContext());
                    Glide.with(MyprofileActivity.this).load(photoPath).error(R.mipmap.headplace).into(circular_head_edit);
                    List<String> paths=new ArrayList<>();
                    paths.add(photoPath);
                    PictureCut.CompressLuban.comPress(getContext(), paths, new PictureCut.CallBack<List<File>>() {
                        @Override
                        public void call(List<File> files) {
                            PictureCut.CompressLuban.toBase64s(files, new PictureCut.CallBack<List<String>>() {
                                @Override
                                public void call(List<String> base64s) {
                                    uploadHead(base64s.get(0));
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                ToastUtil.toast("存储设备异常");
            }
        } else if (requestCode == ABLUM) {
            if (data != null) {//考虑到启动了但是中途取消了
                zProgressHUD.show();
                GlideCacheUtil.getInstance().clearImageAllCache(getContext());
                String path = FileProviderUtil.getFilePath(this, data.getData());
                Glide.with(this).load(path).error(R.mipmap.headplace).into(circular_head_edit);
                photoPath = path;
                List<String> paths=new ArrayList<>();
                paths.add(path);
                PictureCut.CompressLuban.comPress(getContext(), paths, new PictureCut.CallBack<List<File>>() {
                    @Override
                    public void call(List<File> files) {
                        PictureCut.CompressLuban.toBase64s(files, new PictureCut.CallBack<List<String>>() {
                            @Override
                            public void call(List<String> base64s) {
                                uploadHead(base64s.get(0));
                            }
                        });
                    }
                });
            }
        } else if (requestCode == IntentRequestCodeConstant.REQUESTCODE_EDITSINGLE) {
            if (resultCode == IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_NICKNAME) {
                textView_nickname_change.setText(Preference.get(ConstantString.NICK_NAME, "").equals("") ?
                        Preference.get(ConstantString.PHONE, "") : Preference.get(ConstantString.NICK_NAME, ""));

            } else if (resultCode == IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_TRUENAME) {
                mTextView_truename_change.setText(Preference.get(ConstantString.TRUE_NAME, StringConstants.EMPTY_STRING));

            } else if (resultCode == IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_EMAIL) {
                mTextView_mailAdress_change.setText(Preference.get(ConstantString.EMAIL, StringConstants.EMPTY_STRING));
            }

        } else if (requestCode == IntentRequestCodeConstant.REQUESTCODE_RESETPHONE && resultCode == IntentRequestCodeConstant.RESULTCODE_RESETPHONE) {
            mTextView_phone_change.setText(Preference.get(ConstantString.PHONE, StringConstants.EMPTY_STRING));

        }
    }

    private void uploadHead(String base64) {
        if (base64 == null) {
            return;
        }
        if (TextUtils.isEmpty(base64)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("head", base64);
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        BaseBean bean = new BaseBean();
        new OKHttpRequestModel<>(this).okhHttpPost(ConstantUrl.editUserHead, map, bean);
    }


    @Override
    protected void onStart() {
        super.onStart();
        requstDetailsData();
    }

    EditText editText_edituser;

    @Override
    public void beforeTextChanged(int editTextId, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(int editTextId, CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(int editTextId, Editable s) {

    }


    @Override
    protected void onDestroy() {
        if (editText_edituser != null && mIntelligentTextWatcher != null) {
            editText_edituser.removeTextChangedListener(mIntelligentTextWatcher);
        }
        super.onDestroy();
        if(zProgressHUD.isShowing()){
            zProgressHUD.dismiss();
        }
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        if(data instanceof MyProfileBean){
            MyProfileBean bean= (MyProfileBean) data;
            mTextView_address_change.setText(bean.getO().getAddress());
           Glide.with(getContext()).load(bean.getO().getHead()).into(circular_head_edit);
           mTextView_mailAdress_change.setText(bean.getO().getEmlia());
           mTextView_phone_change.setText(bean.getO().getPhone());
           sex=bean.getO().getSex();
            if (sex.equals(StringConstants.STRING_2)) {
                textView_sex_change.setText("女");
            } else {
                textView_sex_change.setText("男");
            }
            textView_nickname_change.setText(bean.getO().getTrue_name());
        }else {
            zProgressHUD.show();
            if (zProgressHUD != null && zProgressHUD.isShowing()) {
                zProgressHUD.dismissWithSuccess("修改成功");
            }
            requstDetailsData();
        }

        if (ConstantUrl.editUserHead.equals(data.getUrl())) {
            Preference.put(ConstantString.HEAD, photoPath);

        } else if (ConstantUrl.EditAddressURl.equals(data.getUrl())) {
            if (mStringBuffer != null) {
                Preference.put(ConstantString.ADDRESS, mStringBuffer.toString());
                mTextView_address_change.setText(province + " " + city + " " + town);
            }

        } else if (ConstantUrl.EditSexURl.equals(data.getUrl())) {
            Preference.put(ConstantString.SEX, sex);
            if (Preference.get(ConstantString.SEX, "").equals(StringConstants.STRING_2)) {
                textView_sex_change.setText("女");
            } else {
                textView_sex_change.setText("男");
            }
            ToastUtil.toast("修改成功");
        }

//        AsyncTask asyncTask = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] params) {
//                SystemClock.sleep(501);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                finish();
//            }
//        };
//        asyncTask.execute();
    }

    @Override
    public void onError(Call call, Exception e, int i) {

        if (zProgressHUD != null && zProgressHUD.isShowing()) {
            zProgressHUD.dismiss();
        }
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        zProgressHUD.show();
        zProgressHUD.dismissWithFailure(c_codeBean.getM());
//        ToastUtil.toast(c_codeBean.getM());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                zProgressHUD.dismiss();
            }
        },700);
//        if (zProgressHUD != null && zProgressHUD.isShowing()) {
//            zProgressHUD.dismiss();
//        }
    }

    @Override
    public void setOnAddressPickListene(String province, String city, String count) {
        this.province = province;
        this.city = city;
        this.town = count;

        requestEditAddress();
    }

    private void requestEditAddress() {
        zProgressHUD.show();
        if (mOkHttpRequestModel == null) {
            mOkHttpRequestModel = new OKHttpRequestModel(this);
        }

        mStringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(province)) {
            mStringBuffer.append(province);
        }
        if (!TextUtils.isEmpty(city)) {
            mStringBuffer.append(city);
        }
        if (!TextUtils.isEmpty(town)) {
            mStringBuffer.append(town);
        }
        if (TextUtils.isEmpty(mStringBuffer.toString())) {
            return;
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put("address", mStringBuffer.toString());
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.EditAddressURl, stringMap, new BaseBean());
    }


    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtil.toast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            photoPath = photofile.getPath();
            imageUri = Uri.fromFile(photofile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                imageUri = FileProvider.getUriForFile(this, getString(R.string.provider), photofile);//通过FileProvider创建一个content类型的Uri
            takePicture(this, imageUri, CODE_CAMERA_REQUEST);
        }
    }

    public void takePicture(Activity activity, Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    final int CAMERA_PERMISSIONS_REQUEST_CODE = 99, CODE_CAMERA_REQUEST = 100, STORAGE_PERMISSIONS_REQUEST_CODE = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imageUri = MyFileProvider.getUriForFile(this, getString(R.string.provider), photofile);//通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    } else {
                        imageUri = Uri.fromFile(photofile);
                    }
                    takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                } else {
                    ToastUtil.toast("请允许打开相机！！");
                }
                break;
            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPick();
                } else {
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
