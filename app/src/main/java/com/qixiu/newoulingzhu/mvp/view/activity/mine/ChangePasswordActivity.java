package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.utils.MD5Util;
import com.qixiu.newoulingzhu.beans.SendCodeBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.presenter.SendCodePresense;
import com.qixiu.newoulingzhu.mvp.view.activity.LoginActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by HuiHeZe on 2017/6/20.
 */

public class ChangePasswordActivity extends TitleActivity implements View.OnClickListener, OKHttpUIUpdataListener<BaseBean> {
    private RelativeLayout relativeLayout_title_changgepsw;
    private MyEditTextView edittext_phone_changepsw, edittext_code_changepsw, edittext_newPsw_changepsw, edittext_newPsw_confirm_changepsw;
    private Button textView_sendcode_psw, btn_confirm_pswchange;
    private String phone, psw, confirmPsw, code;
    private OKHttpRequestModel okmodel;
    private String title = "";

    @Override
    protected void onInitData() {
        title = getIntent().getStringExtra(IntentDataKeyConstant.TITLE);
        setClick();
    }

    private void setClick() {
        textView_sendcode_psw.setOnClickListener(this);
        btn_confirm_pswchange.setOnClickListener(this);
    }

    @Override
    protected void onInitViewNew() {
        okmodel = new OKHttpRequestModel(this);
        textView_sendcode_psw = (Button) findViewById(R.id.textView_sendcode_psw);
        edittext_newPsw_confirm_changepsw = (MyEditTextView) findViewById(R.id.edittext_newPsw_confirm_changepsw);
        edittext_phone_changepsw = (MyEditTextView) findViewById(R.id.edittext_phone_changepsw);
        edittext_code_changepsw = (MyEditTextView) findViewById(R.id.edittext_code_changepsw);
        edittext_newPsw_changepsw = (MyEditTextView) findViewById(R.id.edittext_newPsw_changepsw);
        btn_confirm_pswchange = (Button) findViewById(R.id.btn_confirm_pswchange);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_changepassword;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        title = intent.getStringExtra(ConstantString.TITLE_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_sendcode_psw:
                if (getData(true)) {
                    return;
                }
                phone = edittext_phone_changepsw.getText().toString();
                if (phone.equals("")) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_nothing);
                }
//                if (!CommonUtils.isMobileNO(phone)) {
//                    ToastUtil.showToast(this, "请输入正确的手机号！");
//                }
                Map<String, String> mapSendCode = new HashMap();
                mapSendCode.put("phone", phone);
                mapSendCode.put("type", 2 + "");
                new SendCodePresense(this, mapSendCode, ConstantUrl.sendCodeUrl);
                break;
            case R.id.btn_confirm_pswchange:
                if (getData(false)) {
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("verify", code);
                map.put("verify_id", verify_id);
                map.put("opassword", MD5Util.MD5(confirmPsw));
                map.put("password", (MD5Util.MD5(psw)));
                BaseBean bean = new BaseBean();
                okmodel.okhHttpPost(ConstantUrl.lostPswdUrl, map, bean);
                break;

        }
    }

//    private void startChangePswd() {
//        String token = MD5Util.getToken(SplitStringUtils.cutStringPenult(ConstantUrl.lostPswdUrl, "/"));
//        OkHttpUtils.post().url(ConstantUrl.lostPswdUrl)
////                .addParams("id", Preference.get(ConstantString.USERID, ""))
//                .addParams("phone", phone)
//                .addParams("token",token)
//                .addParams("verify", code)
//                .addParams("verify_id", verify_id)
//                .addParams("opassword", MD5Util.MD5(confirmPsw))
//                .addParams("password", MD5Util.MD5(psw)).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                try {
//                    BaseBean messageBean = GetGson.parseBaseBean(s);
//                    if (messageBean.getM().contains("verify")) {
//                        ToastUtil.showToast(ChangePasswordActivity.this, "验证码不正确");
//                        return;
//                    }
//                    if (messageBean.getC() == 1) {
//                        ToastUtil.showToast(ChangePasswordActivity.this, messageBean.getM());
//                        CommonUtils.startIntent(ChangePasswordActivity.this, LoginActivity.class);
//                        Preference.clearAllFlag();
//                        Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
//                        ChangePasswordActivity.this.finish();
//                    }
//                    ToastUtil.showToast(ChangePasswordActivity.this, messageBean.getM());
//                } catch (Exception e) {
//                }
//            }
//        });
//
//
//    }

    public boolean getData(boolean IS_SENDCODE) {
        phone = edittext_phone_changepsw.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_nothing);
            return true;
        }
        if (!CommonUtils.isMobileNO(phone)) {
            ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_illegal);
            return true;
        }

        if (IS_SENDCODE) {
            return false;
        }
        if (TextUtils.isEmpty(verify_id)) {
            ToastUtil.showToast(this, R.string.edittext_register_input_verrycode_input_rule_not_send);
            return true;
        }
        code = edittext_code_changepsw.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(this, R.string.edittext_register_input_verrycode_input_rule_input_nothing);
            return true;
        }
        psw = edittext_newPsw_changepsw.getText().toString();
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.showToast(this, R.string.edittext_login_input_psw_input_rule_input_nothing);
            return true;
        }
        if (psw.length() < 8||psw.length() > 20) {
            ToastUtil.showToast(this, R.string.edittext_login_input_psw_input_rule_input_length);
            return true;
        }
        confirmPsw = edittext_newPsw_confirm_changepsw.getText().toString();
        if (TextUtils.isEmpty(confirmPsw)) {
            ToastUtil.showToast(this, R.string.edittext_changge_psw_confirm_psw_input_rule_input_nothing);
            return true;
        }

        if (!confirmPsw.equals(psw)) {
            ToastUtil.showToast(this, R.string.edittext_changge_psw_confirm_psw_input_rule_not_same);
            return true;
        }


        return false;
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data instanceof SendCodeBean) {
            SendCodeBean bean = (SendCodeBean) data;
            verify_id = bean.getO().getVerify_id();
            startTimeCountDown(textView_sendcode_psw);
        }
        if (data.getUrl().equals(ConstantUrl.lostPswdUrl)) {
            ToastUtil.showToast(ChangePasswordActivity.this, data.getM());
            CommonUtils.startIntent(ChangePasswordActivity.this, LoginActivity.class);
            Preference.clearAllFlag();
            Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
            ChangePasswordActivity.this.finish();
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.showToast(this, c_codeBean.getM());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTitleView.setTitle(title);
    }
}
