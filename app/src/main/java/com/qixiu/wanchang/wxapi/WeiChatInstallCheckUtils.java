package com.qixiu.wanchang.wxapi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.wanchang.R;

public class WeiChatInstallCheckUtils {
    /**
     *   
     *  判断微信客户端是否存在  
     *   
     *  @return true安装, false未安装  
     */
//    public static boolean isWeChatAppInstalled(Context context) {
//
//        IWXAPI api = WXAPIFactory.createWXAPI(context, "Your WeChat AppId");
//        if (api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
//            return true;
//        } else {
//            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager  
//            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息  
//            if (pinfo != null) {
//                for (int i = 0; i < pinfo.size(); i++) {
//                    String pn = pinfo.get(i).packageName;
//                    if (pn.equalsIgnoreCase("com.tencent.mm")) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//    }

    public static final String wxPackageName = "com.tencent.mm";

    public static boolean checkWXIsInstalled() {
        if (!isAppInstalled(BaseApplication.getContext(), wxPackageName)) {
            ToastUtil.toast(BaseApplication.getContext().getString(R.string.share_notwxclient));
            return false;
        }
        return true;
    }


    /**
     * check the app is installed
     */
    private static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //("没有安装");
            return false;
        } else {
            //("已经安装");
            return true;
        }
    }


//    以上代码在三星S7测试时，会先走SDK的判断，因为SDK的判断始终返回FALSE，因此会走到包名的判断，包名的判断在S7上才有用，如果手机上安装了微信，则返回true,否则返回false。


//    在华为的某型号手机上（记不得了，测试时遇到过，印象很深，因为最开始的方法就是判断包名，
// 在这款手机上就无效，后来改为使用SDK的判断方法实测有效），判断包名的方法无效，因此用以上方法判断时，先走SDK的判断方法，安装了微信，则返回true，否则返回false。


}
