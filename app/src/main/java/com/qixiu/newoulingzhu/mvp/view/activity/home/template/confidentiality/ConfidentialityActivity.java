package com.qixiu.newoulingzhu.mvp.view.activity.home.template.confidentiality;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.DownLoadFileUtils;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CheckStringUtils;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class ConfidentialityActivity extends TitleActivity implements OKHttpUIUpdataListener<BaseBean>, ArshowDialogUtils.ArshowDialogListener {

    private EditText mEt_send_email;
    private TextView mTv_send;
    private OKHttpRequestModel mOkHttpRequestModel;
    private String mId;
    private String mEmail;
    private ZProgressHUD mZProgressHUD;
    //预览的附件
    private String filePath;
    private ZProgressHUD zProgressHUD;
    private String type;//是否能下载的权限判断
    private ImageView imageViewScan;
    private String image;

    @Override
    protected void onInitViewNew() {
        zProgressHUD = new ZProgressHUD(this);
        mTitleView.setTitle("文书下载");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mId = getIntent().getStringExtra(IntentDataKeyConstant.ID);
        mEt_send_email = (EditText) findViewById(R.id.et_send_email);
        mTv_send = (TextView) findViewById(R.id.tv_send);
        imageViewScan =  findViewById(R.id.imageViewScan);
        mTv_send.setOnClickListener(this);
        mZProgressHUD = new ZProgressHUD(this);
        filePath = getIntent().getStringExtra(IntentDataKeyConstant.FILE_PATH);
        image = getIntent().getStringExtra(IntentDataKeyConstant.IMAGE);
        type = getIntent().getStringExtra(IntentDataKeyConstant.TYPE);
        mTitleView.setRightTextVisible(View.VISIBLE);
        mTitleView.setRightText("预览");
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zProgressHUD.show();
                if (!hasPermission(photoPermission)) {
                    hasRequse(1, photoPermission);
                    return;
                }
                DownLoadFileUtils.InitFile.callFile(filePath, new DownLoadFileUtils.FileCallBack() {
                    @Override
                    public void callBack(String path) {
                        zProgressHUD.dismiss();
                        DownLoadFileUtils.openFiles(path, ConfidentialityActivity.this);
                    }
                });
            }
        });
        Glide.with(getContext()).load(image).into(imageViewScan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                mEmail = mEt_send_email.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)) {
                    ToastUtil.toast("请输入内容");
                    return;
                }
                if (!CheckStringUtils.isEmail(mEmail)) {
                    ToastUtil.toast("邮箱格式不正确");
                    return;
                }
                requestSendEmail();
                break;
        }
    }

    private void requestSendEmail() {
        if (type.equals("1")) {
            ArshowDialogUtils.showDialog(getActivity(), "本版块为会员权益，请先开通会员！", "开通会员", "取消", this);
            return;
        }
        if (TextUtils.isEmpty(mId)) {
            return;
        }
        if (mZProgressHUD != null) {
            mZProgressHUD.setMessage("发送中..");
            mZProgressHUD.show();
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.ID, mId);
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put("email", mEmail);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.SendEmail, stringMap, new BaseBean());
    }

    @Override
    protected void onInitData() {
        mEt_send_email.setText(Preference.get(ConstantString.EMAIL, ""));
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        countDown();
    }

    private void countDown() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.ID, mId);
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.countDown, stringMap, new BaseBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_confidentiality;
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
            ToastUtil.toast("发送成功,请前往邮箱查看");
            if (!data.getUrl().equals(ConstantUrl.countDown)) {
                setResult(IntentRequestCodeConstant.RESULTCODE_EMAIL);
                finish();
            }
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("网络异常");
        }
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("发送失败");
        }
    }

    @Override
    public void onClickPositive(DialogInterface dialogInterface, int which) {
        MyMemberActivity.start(getContext());
    }

    @Override
    public void onClickNegative(DialogInterface dialogInterface, int which) {

    }
}
