package com.qixiu.newoulingzhu.mvp.view.activity.mine.edit;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.easeui.bean.StringConstants;
import com.qixiu.newoulingzhu.beans.SendCodeBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.mvp.presenter.SendCodePresense;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class ResetPhoneActivity extends TitleActivity implements OKHttpUIUpdataListener {

    private String mAction;
    private EditText mEt_reset_phone;
    private EditText mEt_checkcode;
    private String phone;
    private String verify_id;
    private TextView mTv_reset_checkcode;
    private OKHttpRequestModel mOkHttpRequestModel;

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("修改手机号");

        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAction = getIntent().getAction();
        mEt_reset_phone = (EditText) findViewById(R.id.et_reset_phone);
        mEt_checkcode = (EditText) findViewById(R.id.et_checkcode);
        mTv_reset_checkcode = (TextView) findViewById(R.id.tv_reset_checkcode);
        Button bt_resel_botton = (Button) findViewById(R.id.bt_resel_botton);
        if (IntentDataKeyConstant.ACTION_EDITPHONE_ORIGINAL.equals(mAction)) {
            mEt_reset_phone.setHint("请输入原手机号码");
            bt_resel_botton.setText("下一步");
        } else {

            mEt_reset_phone.setHint("请输入新手机号码");
            bt_resel_botton.setText("提交");
        }

        bt_resel_botton.setOnClickListener(this);
        mTv_reset_checkcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_resel_botton:
                requestAffirm();
                break;
            case R.id.tv_reset_checkcode:
                phone = mEt_reset_phone.getText().toString().trim();
                requestSendCode();
                break;
        }
    }

    private void requestAffirm() {
        phone=mEt_reset_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, "请输入手机号！");
            return;
        }
        if (!CommonUtils.isMobileNO(phone)) {
            ToastUtil.showToast(this, "您输入的手机号格式不正确！");
            return;
        }
        if (TextUtils.isEmpty(mEt_checkcode.getText().toString().trim())) {
            ToastUtil.toast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(verify_id)) {
            ToastUtil.toast("请先发送验证码");
            return;
        }
        Map<String, String> stringMap = new HashMap<>();

        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        if (IntentDataKeyConstant.ACTION_EDITPHONE_ORIGINAL.equals(mAction)) {
            stringMap.put("phone", phone);
            stringMap.put("verify", mEt_checkcode.getText().toString().trim());
            stringMap.put("verify_id", verify_id);
        } else {
            stringMap.put("ophone", phone);
            stringMap.put("overify", mEt_checkcode.getText().toString().trim());
            stringMap.put("overify_id", verify_id);
        }

        mOkHttpRequestModel.okhHttpPost(IntentDataKeyConstant.ACTION_EDITPHONE_ORIGINAL.equals(mAction) ? ConstantUrl.eitdPhone : ConstantUrl.eitdPhones, stringMap, new BaseBean());
    }


    private void requestSendCode() {

        Map<String, String> mapSendCode = new HashMap();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, "请输入手机号！");
            return;
        }
        if (!CommonUtils.isMobileNO(phone)) {
            ToastUtil.showToast(this, "您输入的手机号格式不正确！");
            return;
        }
        mapSendCode.put("phone", phone);

        mapSendCode.put("type", IntentDataKeyConstant.ACTION_EDITPHONE_ORIGINAL.equals(mAction) ?  StringConstants.STRING_2:StringConstants.STRING_1 );
        new SendCodePresense(this, mapSendCode, ConstantUrl.sendCodeUrl);
    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_resetphone;
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof SendCodeBean) {
            SendCodeBean bean = (SendCodeBean) data;
            verify_id = bean.getO().getVerify_id();
            startTimeCountDown(mTv_reset_checkcode);
        } else {
            BaseBean baseBean = (BaseBean) data;
            if (ConstantUrl.eitdPhone.equals(baseBean.getUrl())) {
                Intent intent = new Intent(this, ResetPhoneActivity.class);
                intent.setAction(IntentDataKeyConstant.ACTION_EDITPHONE_NEW);
                startActivityForResult(intent, IntentRequestCodeConstant.REQUESTCODE_RESETPHONE);

            } else if (ConstantUrl.eitdPhones.equals(baseBean.getUrl())) {
                Preference.put(ConstantString.PHONE, phone);
                setResult(IntentRequestCodeConstant.RESULTCODE_RESETPHONE);
                ToastUtil.toast(baseBean.getM());
                finish();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodeConstant.REQUESTCODE_RESETPHONE && resultCode == IntentRequestCodeConstant.RESULTCODE_RESETPHONE) {
            setResult(IntentRequestCodeConstant.RESULTCODE_RESETPHONE);
            finish();
        }

    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast("提交失败,"+c_codeBean.getM());
    }
}
