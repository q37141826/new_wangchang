package com.qixiu.newoulingzhu.mvp.wight.my_alterdialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.utils.ToastUtil;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class ArshowContextUtil {
    private static Context context = BaseApplication.getContext();
    public static long lastTotalRxBytes = 0;
    public static long lastTimeStamp = 0;
    /**
     * SD卡的路径
     */
    public static final String SDCARD_PATH =
            Environment.getExternalStorageDirectory() + File.separator;

    public enum NetworkState {
        NONE, WIFI, MOBILE
    }
    /**
     * 获取下载的apk资源目录
     *
     * @return
     */
    public static String getResourcePath() {
        File file = new File(ArshowContextUtil.getSandboxPath() + "files" + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
    public static int getColor(@ColorRes int resId) {
        return BaseApplication.getContext().getResources().getColor(resId);

    }

    /**
     * 返回当前程序版本名和号
     */
    public static String[] getVersions(Context context) {
        String[] versions = new String[2];
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versions[0] = pi.versionName;
            versions[1] = pi.versionCode + "";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versions;
    }

    /**
     * 获得版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return Integer.valueOf(getVersions(context)[1]);
    }

    /**
     * 获得版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getVersions(context)[0];
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @deprecated 使用dp2px(int dpVal)
     */
    public static int dp2px(float dpValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @deprecated
     */
    public static int px2dp(float pxValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                BaseApplication.getContext().getResources().getDisplayMetrics());

    }

    /**
     * dp 2 px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                BaseApplication.getContext().getResources().getDisplayMetrics());

    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @return
     */
    public static int isNetworkAvailable() {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo) ||
                                    "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 网络状态
     *
     * @return
     */
    public static NetworkState networkAvailableState() {
        ConnectivityManager connectivity =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return NetworkState.NONE;
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = connectivity.getAllNetworks();
                if (networks != null) {
                    for (Network network : networks) {
                        NetworkInfo networkInfo = connectivity.getNetworkInfo(network);
                        NetworkState x = getNetworkState(networkInfo);
                        if (x != null)
                            return x;
                    }
                }
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo networkInfo : info) {
                        NetworkState x = getNetworkState(networkInfo);
                        if (x != null)
                            return x;
                    }
                }
            }
        }

        return NetworkState.NONE;
    }

    public static void callPhone(Activity activity, String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast("拨号失败,拨打手机号有误");
            return;
        }
//        String[] permissions = {Manifest.permission.CALL_PHONE};
//        if (!hasPermission(permissions)) {
//            hasRequse(activity, 1, permissions);
//            return;
//        }
//        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        BaseApplication.getContext().startActivity(callIntent);

    }

    //检查权限
    public static boolean hasPermission(String... permission) {
        for (String permissiom : permission) {
            if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), permissiom) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //添加权限
    public static void hasRequse(Activity activity, int code, String... permission) {
        ActivityCompat.requestPermissions(activity, permission, 1);
    }

    /**
     * 是否有网络
     *
     * @return true有网
     */
    public static boolean networkAvailable() {
        switch (ArshowContextUtil.networkAvailableState()) {
            case NONE:
                return false;
            default:
                return true;
        }
    }


    /**
     * 获得网络状态
     *
     * @param networkInfo
     * @return
     */
    @Nullable
    private static NetworkState getNetworkState(NetworkInfo networkInfo) {
        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return NetworkState.MOBILE;
            else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return NetworkState.WIFI;
        }
        return null;
    }

    /**
     * Toast提示，为了更好的国际化应该把字符串放在strings文件中
     *
     * @param resId
     */
    public static void toast(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();

    }

    /**
     * Toast提示
     *
     * @param msg
     */
    public static void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置显示的位置
     *
     * @param msg
     * @param gravity
     */
    public static void toastPosition(String msg, int gravity) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    /**
     * 获得速度
     *
     * @param context
     * @return
     */
    public static long netSpeed(Context context) {
        long nowTotalRxBytes = totalRxBytes(context);
        long nowTimeStamp = System.currentTimeMillis();
        long speed =
                (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp);//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    /**
     * 获取流量数据
     *
     * @param context
     * @return
     */
    public static long totalRxBytes(Context context) {
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) ==
                TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes();
    }

    /**
     * 获得屏幕大小，这种方法不需要Activity来获取
     *
     * @return
     */
    public static int[] getScreenSize() {
        int[] size = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }

    /**
     * 获得宽度
     *
     * @return
     */
    public static int getWidthPixels() {
        return getScreenSize()[0];
    }

    /**
     * 获得高度
     *
     * @return
     */
    public static int getHeightPixels() {
        return getScreenSize()[1];
    }

    /**
     * 获得应用所在的沙箱路径
     *
     * @return
     */
    public static String getSandboxPath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return SDCARD_PATH + "Android/data/" + BaseApplication.getContext().getPackageName() + File.separator;
        return null;
    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public static void hideSoftKeybord(View v) {
        /*隐藏软键盘*/
        InputMethodManager imm =
                (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 获取手机信息
     *
     * @return
     */
    public static String getUserAgent() {
        String appUserAgent = null;
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("ARshow");
            ua.append("/version" + getVersionName(context) + "_code" +
                    getVersionCode(context));// App版本信息
            ua.append("/Android");//
            ua.append(android.os.Build.VERSION.RELEASE);//
            ua.append("/" + android.os.Build.MODEL); //
            ua.append("/");//
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 安装Apk
     */
    public static void installApk(String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(localIntent);
    }

    /**
     * 是否安装了对应的apk
     *
     * @param packagename
     * @return
     */
    public static boolean isAppInstalled(String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 启动App
     *
     * @param packageNames
     */
    public static void startApp(List<String> packageNames) {
        try {
            Intent intent = new Intent();
            for (String action : packageNames) {
                intent.setAction(action);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
           Logger.getLogger(ArshowContextUtil.class.getSimpleName(), e.toString());
        }
    }

    /**
     * 卸载应用
     *
     * @param packageName
     */
    public static void uninstallApk(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
