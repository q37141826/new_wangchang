package com.qixiu.newoulingzhu.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.ToastUtil;

import static android.provider.Settings.ACTION_WIRELESS_SETTINGS;

public class NetStatusCheck {

    public static void checkNetWork(Context context){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            checkState_21orNew(context);
        }else {
            checkState_21(context);
        }
    }

    //检测当前的网络状态

    //API版本23以下时调用此方法进行检测
//因为API23后getNetworkInfo(int networkType)方法被弃用
    public static void checkState_21(Context context ){
        //步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
        //NetworkInfo对象包含网络连接的所有信息
        //步骤3：根据需要取出网络连接信息
        //获取WIFI连接的信息
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Boolean isWifiConn = networkInfo.isConnected();

        //获取移动数据连接的信息
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Boolean isMobileConn = networkInfo.isConnected();
        Log.e("netWork",""+isWifiConn+isMobileConn);
        if(!isWifiConn&&!isMobileConn){
//            showDailog(context);
            ToastUtil.toast("网络无连接，请检查网络设置");
        }
    }

    //API版本23及以上时调用此方法进行网络的检测
//步骤非常类似
    public static void checkState_21orNew(Context context ){
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        //用于存放网络连接信息
        StringBuilder sb = new StringBuilder();
        //通过循环将网络信息逐个取出来
        for (int i=0; i < networks.length; i++){
            //获取ConnectivityManager对象对应的NetworkInfo对象
            NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
            sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
        }
        Log.e("netWork",""+networks.length);
        if(networks.length<=0){
//           showDailog(context);
            ToastUtil.toast("网络无连接，请检查网络设置");
        }
    }

    private static void showDailog(Context context) {
        ArshowDialogUtils.showDialog(context, "没有检测到网络，是否跳转设置界面？", "确定", "取消", new ArshowDialogUtils.ArshowDialogListener() {
            @Override
            public void onClickPositive(DialogInterface dialogInterface, int which) {
                Intent intent =new Intent(ACTION_WIRELESS_SETTINGS);
                context.startActivity(intent);
            }

            @Override
            public void onClickNegative(DialogInterface dialogInterface, int which) {
                AppManager.getAppManager().AppExit(context);
            }
        });

    }

}
