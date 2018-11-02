package com.qixiu.newoulingzhu.mvp.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.easeui.utils.MD5Util;
import com.hyphenate.util.NetUtils;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.application.LoginStatus;
import com.qixiu.newoulingzhu.application.NetStatusCheck;
import com.qixiu.newoulingzhu.beans.SendCodeBean;
import com.qixiu.newoulingzhu.beans.login.LoginBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.engine.PlatformLoginEngine;
import com.qixiu.newoulingzhu.engine.bean.UserInfo;
import com.qixiu.newoulingzhu.engine.jpush.JpushEngine;
import com.qixiu.newoulingzhu.mvp.presenter.SendCodePresense;
import com.qixiu.newoulingzhu.mvp.view.activity.base.BaseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.ChangePasswordActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, OKHttpUIUpdataListener, PlatformLoginEngine.PlatformResultListener {
    @BindView(R.id.textViewGetVercode)
    TextView textViewGetVercode;
    @BindView(R.id.edittextRegistPhone)
    MyEditTextView edittextRegistPhone;
    @BindView(R.id.edittextRegistVercode)
    MyEditTextView edittextRegistVercode;
    @BindView(R.id.edittextRegistPsw)
    MyEditTextView edittextRegistPsw;
    @BindView(R.id.textViewUserRuls)
    TextView textViewUserRuls;
    private OKHttpRequestModel okHttpRequestModel;
    private String permissons[] = {Manifest.permission.READ_PHONE_STATE};
    @BindView(R.id.radioLogin)
    RadioButton radioLogin;
    @BindView(R.id.radioRegiste)
    RadioButton radioRegiste;
    @BindView(R.id.textView_forget_password)
    TextView textViewForgetPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.linearlayoutLoginPart)
    LinearLayout linearlayoutLoginPart;
    @BindView(R.id.btnRegiste)
    Button btnRegiste;
    @BindView(R.id.linearlayout_registe_part)
    LinearLayout linearlayoutRegistePart;
    @BindView(R.id.imageViewWeiBo)
    ImageView imageViewWeiBo;
    @BindView(R.id.imageViewQQ)
    ImageView imageViewQQ;
    @BindView(R.id.imageViewWeiChat)
    ImageView imageViewWeiChat;
    @BindView(R.id.linearlayoutAppLogin)
    LinearLayout linearlayoutAppLogin;

    private final int LOGIN = 1, REGISTE = 2;
    @BindView(R.id.edittextLoginPhone)
    MyEditTextView edittextLoginPhone;
    @BindView(R.id.edittextLoginPsw)
    MyEditTextView edittextLoginPsw;
    private int currentState = LOGIN;
    private String phone;
    private String password;
    private Map<String, String> map;
    private String appType;
    private PlatformLoginEngine engine;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected void onInitData() {
        okHttpRequestModel = new OKHttpRequestModel(this);
        refreshState();
        engine = new PlatformLoginEngine(this);
        //获取设备ID
        if (!hasPermission(permissons)) {
            ActivityCompat.requestPermissions(this, permissons, 1);
            return;
        }
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        } catch (Exception e) {
        }
    }

    private void refreshState() {
        if (currentState == LOGIN) {
            linearlayoutAppLogin.setVisibility(View.VISIBLE);
            linearlayoutLoginPart.setVisibility(View.VISIBLE);
            linearlayoutRegistePart.setVisibility(View.GONE);
        } else {
            linearlayoutAppLogin.setVisibility(View.GONE);
            linearlayoutLoginPart.setVisibility(View.GONE);
            linearlayoutRegistePart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onInitView() {
        setClick();
    }

    private void setClick() {
        radioLogin.setOnCheckedChangeListener(this);
        radioRegiste.setOnCheckedChangeListener(this);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == radioRegiste.getId()) {
                currentState = REGISTE;
            } else {
                currentState = LOGIN;
            }
            refreshState();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textView_forget_password, R.id.textViewUserRuls, R.id.textViewGetVercode, R.id.btnLogin, R.id.btnRegiste, R.id.imageViewWeiBo, R.id.imageViewQQ, R.id.imageViewWeiChat})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnLogin:
                //登录
                if (!getInputData()) {
                    return;
                }
                startLogin();
                break;
            case R.id.btnRegiste:
                if (edittextRegistPhone.getText().toString().equals("")) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_nothing);
                    return;
                }
                if (!CommonUtils.isMobileNO(edittextRegistPhone.getText().toString())) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_illegal);
                    return;
                }
                if (TextUtils.isEmpty(edittextRegistPhone.getText().toString())) {
                    ToastUtil.showToast(this, R.string.edittext_register_input_verrycode_input_rule_input_nothing);
                    return;
                }
                if (TextUtils.isEmpty(verify_id)) {
                    ToastUtil.showToast(this, R.string.edittext_register_input_verrycode_input_rule_not_send);
                    return;
                }
                if (TextUtils.isEmpty(edittextRegistPsw.getText().toString())) {
                    ToastUtil.showToast(this, R.string.edittext_changge_psw_confirm_psw_input_rule_input_nothing);
                    return;
                }
                Map<String, String> mapRegister = new HashMap<>();
                mapRegister.put("phone", edittextRegistPhone.getText().toString());
                mapRegister.put("password", MD5Util.MD5(edittextRegistPsw.getText().toString().trim()));
                mapRegister.put("verify_id", verify_id);
                CommonUtils.putDataIntoMap(mapRegister, "device", deviceId);
                mapRegister.put("device_type", 2 + "");
                mapRegister.put("verify", edittextRegistVercode.getText().toString().trim());
                okHttpRequestModel.okhHttpPost(ConstantUrl.registUrl, mapRegister, new BaseBean());
                break;
            case R.id.imageViewWeiBo:
                appType = 3 + "";
                engine.startAuthorize(SinaWeibo.NAME);
                break;
            case R.id.imageViewQQ:
                appType = 1 + "";
                engine.startAuthorize(QQ.NAME);
                break;
            case R.id.imageViewWeiChat:
                appType = 2 + "";
                engine.startAuthorize(Wechat.NAME);
                break;
            case R.id.textViewUserRuls:
                intent = new Intent(this, GoToActivity.class);
                String url = ConstantUrl.hosturl + "/detail/userAgreement.html?URL=" + ConstantUrl.hosturl;
                intent.putExtra(ConstantString.TITLE_NAME, "用户使用协议");
                intent.putExtra(ConstantString.URL, url);
                startActivity(intent);
                break;
            case R.id.textView_forget_password:
                intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra(IntentDataKeyConstant.TITLE, "忘记密码");
                startActivity(intent);
                break;
            case R.id.textViewGetVercode:
                if (edittextRegistPhone.getText().toString().equals("")) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_nothing);
                    return;
                }
                if (!CommonUtils.isMobileNO(edittextRegistPhone.getText().toString())) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_illegal);
                    return;
                }
                Map<String, String> mapSendCode = new HashMap<>();
                mapSendCode.put("phone", edittextRegistPhone.getText().toString());
                mapSendCode.put("type", 1 + "");
                new SendCodePresense(this, mapSendCode, ConstantUrl.sendCodeUrl);
                break;

        }
    }

    private void startLogin() {
        map = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("device_type", "2");
        map.put("phone", phone.trim());
        map.put("password", MD5Util.MD5(password.trim()));
        map.put("device", deviceId == null ? "" : deviceId);
        JpushEngine.setAlias(this, deviceId == null ? "deng" : deviceId);
        LoginBean bean = new LoginBean();
        okHttpRequestModel.okhHttpPost(ConstantUrl.loginUrl, map, bean);
    }

    public boolean getInputData() {
        phone = edittextLoginPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast(R.string.edittext_login_input_id_input_rule_input_nothing);
            return false;
        }
        if (!CommonUtils.isMobileNO(phone)) {
            ToastUtil.toast("请输入正确的手机号 ");
            return false;
        }
        password = edittextLoginPsw.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toast(R.string.edittext_login_input_psw_input_rule_hint);
            return false;
        }
        if ((password.length() < 8 || (password.length() > 20))) {
            ToastUtil.showToast(this, R.string.edittext_login_input_psw_input_rule_input_length);
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof LoginBean) {
            LoginStatus.logged((LoginBean) data);
            finish();
            MainActivity.start(this);
            HyEngine.login(this, Preference.get(ConstantString.USERID, StringConstants.EMPTY_STRING), false, StringConstants.EMPTY_STRING, null);
            HyEngine.addConnectionListener(new MyConnectionListener());
        }
        if (data instanceof SendCodeBean) {
            SendCodeBean bean = (SendCodeBean) data;
            verify_id = bean.getO().getVerify_id();
            startTimeCountDown(textViewGetVercode);
        }
        if (data instanceof BaseBean) {
            BaseBean bean = (BaseBean) data;
            ToastUtil.toast(bean.getM());
            if (bean.getUrl().equals(ConstantUrl.registUrl)) {
                edittextRegistPsw.setText("");
                edittextRegistVercode.setText("");
                edittextRegistPhone.setText("");
                verify_id = null;
            }
        }
        if (((BaseBean) data).getUrl().equals(ConstantUrl.outLoginUrl)) {
            Preference.clearAllFlag();
            Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
            AppManager.getAppManager().finishAllActivity();
            CommonUtils.startIntent(this, LoginActivity.class);
            return;
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasPermission(permissons)) {
            try {
                TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = tm.getDeviceId();
            } catch (Exception e) {
            }
        }else {
            finish();
        }
    }


    @Override
    public void onCancel() {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        Map<String, String> map = new HashMap();
        map.put("register_type", appType);
        map.put("head", userInfo.getUserIcon());
        map.put("username", userInfo.getUserName());
        map.put("type", 2 + "");
        CommonUtils.putDataIntoMap(map, "device", deviceId);
        CommonUtils.putDataIntoMap(map, "sex", userInfo.getGender());
        map.put("party_key", userInfo.getUserId());
        okHttpRequestModel.okhHttpPost(ConstantUrl.appLoginUrl, map, new LoginBean());

    }


    public class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
//                Toast.makeText(getContext(),"账号已被移除",Toast.LENGTH_SHORT).show();
                HyEngine.logout();
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 显示帐号在其他设备登录
                        Toast.makeText(getContext(), "您的账号正在另一台设备上登录", Toast.LENGTH_SHORT).show();
                    }
                });
                HyEngine.logout();
                if (AppManager.getAppManager().currentActivity() == null) {
                    return;
                }
                requestOutlog();
            } else {
                if (NetUtils.hasNetwork(getContext())) {
                    //    ToastUtil.toast("连接不到聊天服务器");
                } else {
                    //当前网络不可用，请检查网络设置
//                    ToastUtil.toast("请检查你的网络");
                }

            }
        }
    }


    public void requestOutlog() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        okHttpRequestModel.okhHttpPost(ConstantUrl.outLoginUrl, map, new BaseBean());
    }

}
