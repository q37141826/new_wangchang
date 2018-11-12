package com.qixiu.newoulingzhu.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;

import com.hyphenate.easeui.bean.StringConstants;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.jpush.JpushEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;

import static com.qixiu.newoulingzhu.receiver.JPushReceiver.JPushAction.KEY_MESSAGE;


/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class JPushReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent pushintent=new Intent(context,PushService.class);//启动极光推送的服务
        context.startService(pushintent);
        this.context = context;
        Bundle bundle = intent.getExtras();
        ArshowLog.d(getClass(), "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " +
                printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            ArshowLog.d(getClass(), "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            if(Preference.getBoolean(ConstantString.IS_SHOCK)){
                Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            ArshowLog.d(getClass(), "[MyReceiver] 接收到推送下来的自定义消息: " +
                    bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            ArshowLog.d(getClass(), "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            String titile = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            //第一时间收到推送后的自动处理
            if (titile.contains("")) {
            }//可以根据tiitle包含的关键字进行不同的被动处理
            //这里没有判断直接广播了
            Intent mainIntent = new Intent(ConstantString.broadCastAction);
            context.sendBroadcast(mainIntent);
            //桌面icon右上角数量
            int messageCount = Preference.get(ConstantString.broadCastMessageCount, 0);
            messageCount++;
            Preference.put(ConstantString.broadCastMessageCount, messageCount);
            int badgeCount = messageCount;
//            if (!ShortcutBadger.applyCount(context, badgeCount)) {
//                context.startService(
//                        new Intent(context, BadgeIntentService.class).putExtra("badgeCount", badgeCount)
//                );
//            }
//
//            if (AppManager.getAppManager().getActivitySize() > 0) {
//                Activity activity = AppManager.getAppManager().currentActivity();
//                if (activity instanceof SystemMessageActivity) {
//                    SystemMessageActivity messageListActivity = (SystemMessageActivity) activity;
//                    messageListActivity.onRefresh();
//                }
//            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            if (TextUtils.isEmpty(Preference.get(IntentDataKeyConstant.ID, ConstantString.STRING_EMPTY))) {
                if (AppManager.getAppManager().getActivitySize() <= 0) {
                    JpushEngine.startPage(context);
                    return;
                } else {
                    ToastUtil.toast("请登录");
                }

            } else {
                if (AppManager.getAppManager().getActivitySize() <= 0) {

                    MainActivity.MAINMESSAGELIST_FLAG = StringConstants.STRING_1;
                    JpushEngine.startPage(context);
                    return;
                } else {
                    Activity activity = AppManager.getAppManager().currentActivity();
//                    if (!(activity instanceof SystemMessageActivity)) {
//                        JpushEngine.startActivity(activity, SystemMessageActivity.class);
//                    }
                }

            }
            //点击通知消息后的处理


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            ArshowLog.d(getClass(), "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
                    bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected =
                    intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            ArshowLog.w(getClass(),
                    "[MyReceiver]" + intent.getAction() + " connected state change to " +
                            connected);
        } else {
            ArshowLog.d(getClass(), "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 打印所有的 intent extra 数据
     */
    private String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    ArshowLog.i(JPushReceiver.class, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    ArshowLog.e(JPushReceiver.class, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 处理消息
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        if (!ConfigInfo.isBackground(context)) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(JPushAction.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(KEY_MESSAGE, message);
            if (!TextUtils.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(JPushAction.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }

    }

    public static class JPushAction {

        /**
         * 用于接收广播的Action
         */
        public static String MESSAGE_RECEIVED_ACTION;
        public static final String KEY_MESSAGE = "message";
        public static final String KEY_EXTRAS = "extras";

    }

}
