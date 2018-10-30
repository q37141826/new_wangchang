package com.qixiu.newoulingzhu.engine.jpush;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyphenate.easeui.bean.StringConstants;
import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.mvp.view.activity.guidepage.StartPageActivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class JpushEngine {


    /**
     * 初始化JPush
     *
     * @param context
     */
    public static void initJPush(Context context) {
        JPushInterface.init(context);
        String machineCode = getMachineCode();
        if (machineCode != null)
            setAlias(context, machineCode);

    }

    /**
     * 设置别名，针对别名推送消息
     *
     * @param context
     * @param alias   别名
     */
    public static void setAlias(Context context, String alias) {
        JPushInterface.setAlias(context, alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e(getClass().getSimpleName(), i + "---" + s);
            }
        });
    }

    public static String getMachineCode() {

        final TelephonyManager tm = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String uniqueId = deviceUuid.toString();
        return tmDevice;
    }


    public static void startActivityByType(Context context, String type, String id) {
        Intent intent = new Intent();
        if (StringConstants.STRING_1.equals(type)) {
//            intent.setClass(context, ConsultDetailActivity.class);
            intent.putExtra(ConstantString.MSG_ID, id);
        } else if (StringConstants.STRING_2.equals(type)) {
//            intent.setClass(context, GoodDetailActivity.class);
//            intent.putExtra(IntentDataKeyConstant.GOODS_ID, id);
        } else {
//            intent.setClass(context, OrderDetailsActivity.class);
//            intent.putExtra(IntentDataKeyConstant.ORDER_ID, id);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startPage(Context context) {
        startActivity(context, StartPageActivity.class);
    }

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
