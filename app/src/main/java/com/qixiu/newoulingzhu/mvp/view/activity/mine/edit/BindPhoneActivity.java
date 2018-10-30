package com.qixiu.newoulingzhu.mvp.view.activity.mine.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qixiu.newoulingzhu.beans.SendCodeBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.presenter.SendCodePresense;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.qixiu.newoulingzhu.constant.ConstantUrl.sendCodeUrl;

public class BindPhoneActivity extends RequstActivity {


    @BindView(R.id.edittextPhoneBind)
    MyEditTextView edittextPhoneBind;
    @BindView(R.id.edittextPswBind)
    MyEditTextView edittextPswBind;
    @BindView(R.id.edittextVercodeBind)
    MyEditTextView edittextVercodeBind;
    @BindView(R.id.btnGetVercode)
    Button btnGetVercode;
    @BindView(R.id.btnBindPhone)
    Button btnBindPhone;
    @BindView(R.id.textViewUserRules)
    TextView textViewUserRules;
    private String phone;
    private String psw;
    private String vercode;

    public static void start(Context context) {
        context.startActivity(new Intent(context, BindPhoneActivity.class));
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("绑定手机");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof SendCodeBean) {
            SendCodeBean bean = (SendCodeBean) data;
            verify_id = bean.getO().getVerify_id();
            startTimeCountDown(btnGetVercode);
        }
        ToastUtil.toast(data.getM());
        if (data.getUrl().equals(ConstantUrl.bingUserPhone)) {
            finish();
            Preference.put(ConstantString.PHONE, edittextPhoneBind.getText().toString());
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
        ToastUtil.toast(c_codeBean.getM());
    }


    public boolean getAllData() {
        phone = edittextPhoneBind.getText().toString().trim();
        psw = edittextPswBind.getText().toString().trim();
        vercode = edittextVercodeBind.getText().toString().trim();
        if (TextUtils.isEmpty(edittextPhoneBind.getText().toString())) {
            ToastUtil.toast(R.string.edittext_login_input_id_input_rule_input_nothing);
            return false;
        }
        if (!CommonUtils.isMobileNO(edittextPhoneBind.getText().toString())) {
            ToastUtil.toast(R.string.edittext_login_input_id_input_rule_input_illegal);
            return false;
        }
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.toast(R.string.edittext_login_input_psw_input_rule_hint);
            return false;
        }
        if (TextUtils.isEmpty(verify_id)) {
            ToastUtil.toast(R.string.edittext_register_input_verrycode_input_rule_not_send);
            return false;
        }
        if (TextUtils.isEmpty(edittextVercodeBind.getText().toString().trim())) {
            ToastUtil.toast(R.string.edittext_register_input_verrycode_input_rule_input_nothing);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnGetVercode, R.id.btnBindPhone, R.id.textViewUserRules})
    public void onViewClicked(View view) {
        Map<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnGetVercode:
                if (TextUtils.isEmpty(edittextPhoneBind.getText().toString())) {
                    ToastUtil.toast(R.string.edittext_login_input_id_input_rule_input_nothing);
                    return;
                }
                if (!CommonUtils.isMobileNO(edittextPhoneBind.getText().toString())) {
                    ToastUtil.toast(R.string.edittext_login_input_id_input_rule_input_illegal);
                    return;
                }
                map.put("type", 1 + "");
                map.put("phone", edittextPhoneBind.getText().toString() + "");
                new SendCodePresense(this, map, sendCodeUrl);
                break;
            case R.id.btnBindPhone:
                if (!getAllData()) {
                    return;
                }
                map.put("uid", Preference.get(ConstantString.USERID, ""));
                map.put("phone", phone);
                map.put("verify", vercode);
                map.put("verify_id", verify_id);
                post(ConstantUrl.bingUserPhone, map, new BaseBean());
                break;
            case R.id.textViewUserRules:
                Intent intent = new Intent(this, GoToActivity.class);
                String url = ConstantUrl.hosturl + "/detail/userAgreement.html?URL=" + ConstantUrl.hosturl;
                intent.putExtra(ConstantString.TITLE_NAME, "用户使用协议");
                intent.putExtra(ConstantString.URL,url);
                startActivity(intent);
                break;
        }
    }
}
