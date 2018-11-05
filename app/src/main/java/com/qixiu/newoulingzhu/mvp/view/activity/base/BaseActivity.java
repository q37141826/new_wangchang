package com.qixiu.newoulingzhu.mvp.view.activity.base;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.application.NetStatusCheck;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    public String verify_id = "";
    private FragmentTransaction mFragmentTransaction;
    protected FragmentManager mSupportFragmentManager;
    protected String[] photoPermission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public int windowHeight, windowWith;
    private Toolbar toolbar;
    private TextView tvTitle;
    BroadcastReceiver receiver;
    public String deviceId;

    public Context getContext(){
        return this;
    }
    public Activity getActivity(){
        return this;
    }

    //检查权限
    public boolean hasPermission(String... permission) {
        for (String permissiom : permission) {
            if (ActivityCompat.checkSelfPermission(this, permissiom) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //添加权限
    public void hasRequse(int code, String... permission) {
        ActivityCompat.requestPermissions(this, permission, 1);
    }

    /**
     * 切换Fragment，与上面的方法不同的是判断是否需要添加堆栈中
     *
     * @param fragment
     * @param resId
     * @param arguments
     * @param isBack
     */
    public void switchFragment(Fragment thisFragment, Fragment fragment, int resId,
                               Bundle arguments, boolean isBack) {
        switchFragment(thisFragment, fragment, resId, arguments, isBack, false);
    }

    /**
     * @param fragment  要替换的Fragment
     * @param arguments 需要携带的参数
     * @param isBack    是否添加到回退栈
     * @param isHide    是否隐藏
     */
    public void switchFragment(Fragment thisFragment, Fragment fragment, int resId,
                               Bundle arguments, boolean isBack, boolean isHide) {
        switchFragment(thisFragment, fragment, resId, arguments, isBack, null, isHide);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param resId
     * @param arguments
     * @param isBack
     * @param tag       Fragment的标签
     * @param isHide
     */
    public void switchFragment(Fragment thisFragment, Fragment fragment, int resId,
                               Bundle arguments, boolean isBack, String tag, boolean isHide) {
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        mFragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);// 设置动画效果
        mFragmentTransaction.replace(resId, fragment);
        if (isBack)
            mFragmentTransaction.addToBackStack(tag);
        if (isHide)
            mFragmentTransaction.hide(thisFragment);
        mFragmentTransaction.commit();
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param resId
     * @param arguments
     */
    public void switchFragment(Fragment fragment, int resId, Bundle arguments) {
        switchFragment(null, fragment, resId, arguments, false, null, false);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param resId
     * @param arguments
     * @param tag
     */
    public void switchFragment(Fragment fragment, int resId, Bundle arguments, String tag) {
        switchFragment(null, fragment, resId, arguments, false, tag, false);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param resId
     */
    public void switchFragment(Fragment fragment, int resId) {
        switchFragment(null, fragment, resId, null, false, null, false);
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param resId
     * @param tag
     */
    public void switchFragment(Fragment fragment, int resId, String tag) {
        switchFragment(null, fragment, resId, null, false, tag, false);
    }

    /**
     * 添加Fragment
     *
     * @param layoutId
     * @param fragment
     * @param tag      Fragment的标签
     */
    protected void addFragment(int layoutId, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(layoutId, fragment, tag).commit();
    }

    protected void addFragment(int layoutId, Fragment fragment, String tag, Bundle bundle) {
        if (fragment != null) {

            fragment.setArguments(bundle);

        }
        addFragment(layoutId, fragment, tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        AppManager.getAppManager().addActivity(this);

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics mtr = new DisplayMetrics();
        defaultDisplay.getMetrics(mtr);
        mContext = this;
        ButterKnife.bind(this);
        windowHeight = mtr.heightPixels;
        windowWith = mtr.widthPixels;
        Log.e("windowheight:windowWith", windowHeight + "---" + windowWith);
        Log.e("where", this.getClass().getSimpleName());
        mSupportFragmentManager = getSupportFragmentManager();
        setScreenOrentation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        onInitView();
        onInitData();
        //将内容延伸到状态栏下面兵并且  不影响  虚拟键
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            deviceId = tm.getDeviceId();
            Log.e("deviceId",deviceId);
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.qixiu.example.broadcast.normal");
        intentFilter.setPriority(600);
        registerReceiver(receiver, intentFilter);
            NetStatusCheck. checkNetWork(getContext());//检查网络状况
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(receiver);
    }

    protected abstract void onInitData();

    protected abstract void onInitView();


    protected abstract int getLayoutResource();

    TextView codeText;
    public Timer timer;
    private String originalText;
    public void startTimeCountDown(TextView codeText) {
        this.codeText = codeText;
//        String token = MD5Util.getToken(Constant.SendCodeTag);
        //3表示是验证码登录
        if (!TextUtils.isEmpty(codeText.getText())) {
            this.originalText = codeText.getText().toString();
        }
        timer = new Timer();
        timer.start();
    }

    public void onNavigateUpClicked() {
        onBackPressed();
    }


    public Toolbar getToolBar() {
        return toolbar;
    }


    private class Timer extends CountDownTimer {

        public Timer() {

            super(60 * 1000, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("test", "??");
            codeText.setText(millisUntilFinished / 1000 + "秒后重发");
            codeText.setEnabled(false);
        }

        @Override
        public void onFinish() {
            if (!TextUtils.isEmpty(originalText)) {
                codeText.setText(originalText);
            } else {
                codeText.setText("发送验证码");
            }

            codeText.setEnabled(true);

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            codeText.setText("发送验证码");
            codeText.setEnabled(true);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
        AppManager.getAppManager().removeActivity(this);
    }

    //提示，参数在ActivityInfo里面
    public void setScreenOrentation(int screenOrentation) {
        setRequestedOrientation(screenOrentation);
    }
}
