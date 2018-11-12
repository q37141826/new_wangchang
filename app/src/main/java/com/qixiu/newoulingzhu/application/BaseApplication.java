package com.qixiu.newoulingzhu.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.bean.StringConstants;
import com.hyphenate.util.NetUtils;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.engine.jpush.JpushEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.LoginActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.service.HyReceiveService;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class BaseApplication extends MultiDexApplication implements OKHttpUIUpdataListener {
    private static BaseApplication app;
    private static Context mContext;
    private ZProgressHUD mZProgressHUD;
    private OKHttpRequestModel okHttpRequestModel;
    public static int NOTICE_NUM = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        okHttpRequestModel = new OKHttpRequestModel(this);
        app = this;
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }//加入这段话  用低版本的权限模式
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        initSdk();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        // CrashHandler.getInstance().init(this);

    }



    public static BaseApplication getApp() {
        return app;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isLoginToLongin(Activity activity) {
        if (TextUtils.isEmpty(Preference.get("id", ""))) {
//            Intent intent = new Intent(activity, LoginActivity.class);
//            activity.startActivity(intent);
            return false;

        } else {
            return true;
        }

    }

    private void initSdk() {
        //初始化ShareSDK
        try {
            ShareSDK.initSDK(getContext());
        } catch (Exception e) {
        }
//        //初始化极光推送
        JpushEngine.initJPush(getContext());
        HyEngine.init();
        HyEngine.addConnectionListener(new MyConnectionListener());
        Intent intent = new Intent(this, HyReceiveService.class);
////        EMChatManager manager= EMClient.getInstance().chatManager();
        startService(intent);
//        XutilsModel.initXutil(this);
        if (!TextUtils.isEmpty(Preference.get(ConstantString.USERID, ""))) {
            Map<String, String> map = new HashMap<>();
            map.put("uid", Preference.get(ConstantString.USERID, ""));
            okHttpRequestModel.okhHttpPost(ConstantUrl.checkUserExsit, map, new BaseBean());
            EMClient.getInstance().login(Preference.get(ConstantString.USERID, StringConstants.EMPTY_STRING), "123456", new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.e("main", "登录聊天服务器成功！");
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    Log.d("main", "登录聊天服务器失败！");
                }
            });
        }

    }

    @Override
    public void onSuccess(Object data, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
        if (((BaseBean) data).getUrl().equals(ConstantUrl.outLoginUrl)) {
            Preference.clearAllFlag();
            Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
            AppManager.getAppManager().finishAllActivity();
            CommonUtils.startIntent(this, LoginActivity.class);
            Toast.makeText(getContext(), "您的账号正在另一台设备上登录", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (((BaseBean) data).getUrl().equals(ConstantUrl.checkUserExsit)) {
//        }

        return;
    }

    @Override
    public void onError(okhttp3.Call call, Exception e, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
    }


    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismiss();
        }
        if (c_codeBean.getUrl().equals(ConstantUrl.checkUserExsit)) {
//            AppManager.getAppManager().finishAllActivity();
            HyEngine.logout();
            startActivity(new Intent(this, LoginActivity.class));
        }
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
                // 显示帐号在其他设备登录
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
//        mZProgressHUD = new ZProgressHUD(AppManager.getAppManager().currentActivity());
//        mZProgressHUD.setMessage("退出登录...");
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        new OKHttpRequestModel<>(BaseApplication.this).okhHttpPost(ConstantUrl.outLoginUrl, map, new BaseBean());
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return app;
    }



}
