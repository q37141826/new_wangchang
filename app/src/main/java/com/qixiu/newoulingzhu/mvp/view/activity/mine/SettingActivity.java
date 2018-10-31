package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.LoginActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.newoulingzhu.utils.VersionCheckUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.qixiu.utils.PictureCut;
import com.qixiu.wanchang.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SettingActivity extends TitleActivity implements OKHttpUIUpdataListener<BaseBean> {
    private RelativeLayout relativeLayout_clean_setting, relativeLayout_messageNotice_setting,
            relativeLayout_feedback_setting, relativeLayout_oboutus_setting,
            relativeLayout_versionCheck_setting;
    private ZProgressHUD zProgressHUD;

    private OKHttpRequestModel okHttpRequestModel;
    private Button btn_out_login;
    private ImageView imageView_messageNotice;
    private String type;
    private RelativeLayout relativeLayout_changepsw;
    private TextView textViewVersionSetting;
    private ImageView imageView_voiceNotice;
    private ImageView imageView_shockNotice;
    private RelativeLayout relativeLayout_voice_setting;
    private RelativeLayout relativeLayout_shockNotice_setting;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onInitViewNew() {
        okHttpRequestModel = new OKHttpRequestModel(this);
        zProgressHUD = new ZProgressHUD(this);
        imageView_messageNotice = (ImageView) findViewById(R.id.imageView_messageNotice);
        btn_out_login = (Button) findViewById(R.id.btn_out_login);
        relativeLayout_clean_setting = (RelativeLayout) findViewById(R.id.relativeLayout_clean_setting);
        relativeLayout_messageNotice_setting = (RelativeLayout) findViewById(R.id.relativeLayout_messageNotice_setting);

        relativeLayout_voice_setting = (RelativeLayout) findViewById(R.id.relativeLayout_voice_setting);
        relativeLayout_shockNotice_setting = (RelativeLayout) findViewById(R.id.relativeLayout_shockNotice_setting);

        imageView_voiceNotice = findViewById(R.id.imageView_voiceNotice);
        imageView_shockNotice = findViewById(R.id.imageView_shockNotice);

        relativeLayout_feedback_setting = (RelativeLayout) findViewById(R.id.relativeLayout_feedback_setting);
        relativeLayout_oboutus_setting = (RelativeLayout) findViewById(R.id.relativeLayout_oboutus_setting);
        relativeLayout_changepsw = (RelativeLayout) findViewById(R.id.relativeLayout_changepsw);
        relativeLayout_versionCheck_setting = (RelativeLayout) findViewById(R.id.relativeLayout_versionCheck_setting);
        textViewVersionSetting = findViewById(R.id.textViewVersionSetting);
        textViewVersionSetting.setText("V  " + getVerName(getContext()));
        initClick();
        imageView_voiceNotice.setImageResource(Preference.getBoolean(ConstantString.IS_VOICE)?R.mipmap.setting_switch_open2x :R.mipmap.setting_switch_close2x);
        imageView_shockNotice.setImageResource(Preference.getBoolean(ConstantString.IS_SHOCK)?R.mipmap.setting_switch_open2x :R.mipmap.setting_switch_close2x);
    }

    private void initClick() {
        relativeLayout_clean_setting.setOnClickListener(this);
        relativeLayout_messageNotice_setting.setOnClickListener(this);
        relativeLayout_feedback_setting.setOnClickListener(this);
        relativeLayout_oboutus_setting.setOnClickListener(this);
        relativeLayout_versionCheck_setting.setOnClickListener(this);
        btn_out_login.setOnClickListener(this);
        relativeLayout_changepsw.setOnClickListener(this);

        relativeLayout_voice_setting.setOnClickListener(this);
        relativeLayout_shockNotice_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent1;
        switch (v.getId()) {
            case R.id.relativeLayout_clean_setting:
                File filesDir = getFilesDir();
                zProgressHUD.show();
                cleanapp(filesDir);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PictureCut.deleteAllImage();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                zProgressHUD.dismiss();
                                ToastUtil.showToast(SettingActivity.this, "清理完毕");
                            }
                        });
                    }
                }).start();
                break;
            case R.id.relativeLayout_feedback_setting:
//                CommonUtils.startIntent(this, FeedBackActivity.class);
                break;
            case R.id.relativeLayout_oboutus_setting:
//                intent1 = new Intent(this, OboutUsActivity.class);
//                intent1.putExtra(ConstantString.TITLE_NAME, "关于我们");
//                startActivity(intent1);
                break;
            case R.id.btn_out_login:
                setDialog("是否退出登录");
                break;
            case R.id.relativeLayout_versionCheck_setting:

                VersionCheckUtil.checkVersion(this, this, new VersionCheckUtil.IsNewVerSion() {
                    @Override
                    public void call(boolean isNew) {
                        if(isNew){
                            ToastUtil.toast("当前已是最新版本");
                        }
                    }
                });
                break;

            case R.id.relativeLayout_messageNotice_setting:
                //// TODO: 2017/11/1 开启或者关闭消息提醒的接口
                Map<String, String> map = new HashMap<>();
                map.put("uid", Preference.get(ConstantString.USERID, ""));
                if (Preference.get(ConstantString.SWITCH, "").equals("1")) {
                    type = 2 + "";
                } else {
                    type = 1 + "";
                }
                map.put("type", type);
                okHttpRequestModel.okhHttpPost(ConstantUrl.messageSwitchUrl, map, new BaseBean());
                break;
            case R.id.relativeLayout_changepsw:
                intent1 = new Intent(getContext(), ChangePasswordActivity.class);
                intent1.putExtra(IntentDataKeyConstant.TITLE, "修改密码");
                startActivity(intent1);
                break;

            case R.id.relativeLayout_voice_setting:
                Preference.putBoolean(ConstantString.IS_VOICE, !Preference.getBoolean(ConstantString.IS_VOICE));
                imageView_voiceNotice.setImageResource(Preference.getBoolean(ConstantString.IS_VOICE) ? R.mipmap.setting_switch_open2x : R.mipmap.setting_switch_close2x);
                if (Preference.getBoolean(ConstantString.IS_VOICE)) {
                    Uri noticeVoice = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), noticeVoice);
                    r.play();
                }
                break;
            case R.id.relativeLayout_shockNotice_setting:
                Preference.putBoolean(ConstantString.IS_SHOCK, !Preference.getBoolean(ConstantString.IS_SHOCK));
                imageView_shockNotice.setImageResource(Preference.getBoolean(ConstantString.IS_SHOCK) ? R.mipmap.setting_switch_open2x : R.mipmap.setting_switch_close2x);
                if (Preference.getBoolean(ConstantString.IS_SHOCK)) {
                    Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000);
                }
                break;
        }
    }

    private void cleanapp(File filesDir) {
        File[] files = filesDir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else {
                cleanapp(file);
            }
        }
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("设置");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setState(Preference.get(ConstantString.SWITCH, 1 + ""));
    }

    private void startOutLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("id", Preference.get(ConstantString.USERID, ""));
        okHttpRequestModel.okhHttpPost(ConstantUrl.outLoginUrl, map, new BaseBean());
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setting;
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data.getUrl().equals(ConstantUrl.outLoginUrl)) {
            CommonUtils.startIntent(this, LoginActivity.class);
            Preference.clearAllFlag();
            Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
            //        //退出环信
            EMClient.getInstance().logout(true);
            finish();
            try {
                AppManager.getAppManager().finishActivity(MainActivity.class);
            } catch (Exception e) {
            }
            return;
        }
        if (data.getUrl().equals(ConstantUrl.messageSwitchUrl)) {
            setState(type);
            Preference.put(ConstantString.SWITCH, type);
        }

    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }

    private void setDialog(String message) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> map = new HashMap<>();
                map.put("uid", Preference.get(ConstantString.USERID, ""));
                okHttpRequestModel.okhHttpPost(ConstantUrl.outLoginUrl, map, new BaseBean());
                dialog.dismiss();
            }
        });

        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void setState(String state) {
        if (state.equals(1 + "")) {
            imageView_messageNotice.setImageResource(R.mipmap.setting_switch_open2x);
        } else {
            imageView_messageNotice.setImageResource(R.mipmap.setting_switch_close2x);
        }
    }


    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
