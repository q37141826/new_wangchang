package com.qixiu.newoulingzhu.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.wanchang.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.hyphenate.easeui.ui.EaseChatFragment.EESSAGE_KEY;
import static com.hyphenate.easeui.ui.EaseChatFragment.MESSAGE_SUCCESS_BROADCAST;

public class HyReceiveService extends Service {

    public HyReceiveService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                Intent intent = new Intent(IntentDataKeyConstant.BROADCAST_MESSAGE);
                sendBroadcast(intent);
                //重置透传消息的UI状态
                Intent intent2 = new Intent(MESSAGE_SUCCESS_BROADCAST);
                sendBroadcast(intent2);
                BaseApplication.NOTICE_NUM++;
                ShortcutBadger.applyCount(getApplicationContext(), BaseApplication.NOTICE_NUM);
                if (AppManager.getAppManager().currentActivity() == null || !(AppManager.getAppManager().currentActivity() instanceof ChatActivity)) {
                    //这一块是系统提示弹窗
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.logo)//提示框的图标
                            .setContentTitle("有消息来啦!")
                            .setContentText("有消息来啦!");

                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.setAction(IntentDataKeyConstant.NOTICE_GOTO_CHATE);
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(
                            getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    PendingIntent resultPendingIntent=  PendingIntent.getBroadcast(getApplicationContext(), 1,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    PendingIntent contentIntent = PendingIntent.getActivities(
//                            getApplicationContext(),
//                            0,
//                            makeIntentStack(getApplicationContext()),
//                            PendingIntent.FLAG_CANCEL_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    Notification notification = mBuilder.build();
                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotifyMgr.notify(1, notification);
                    ShortcutBadger.applyCount(getApplicationContext(), HyEngine.getUnReadMsgCount());

                }
                //下面是提示音
                if (Preference.getBoolean(ConstantString.IS_VOICE)) {
                    Uri noticeVoice = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), noticeVoice);
                    r.play();
                }
                //下面是震动
                if (Preference.getBoolean(ConstantString.IS_SHOCK)) {
                    Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(getApplicationContext().VIBRATOR_SERVICE);
//                    vibrator.vibrate(1000);
                    long[] pattern = new long[] { 0, 180, 80};
                    vibrator.vibrate(pattern,-1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                Intent intent = new Intent(MESSAGE_SUCCESS_BROADCAST);
                if(messages.size()!=0){
                    intent .putExtra(EESSAGE_KEY,messages.get(0));
                    EventBus.getDefault().post(messages.get(0));
                }
//                sendBroadcast(intent);
                ToastUtil.toast("收到透传消息");
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                ToastUtil.toast("收到已读回执");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                ToastUtil.toast("收到已送达回执");
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                ToastUtil.toast("消息被撤回");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                ToastUtil.toast("消息状态变动");
            }
        };
        HyEngine.addMessageListenner(msgListener);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        HyEngine.addMessageListenner(msgListener);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    EMMessageListener msgListener;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    Intent[] makeIntentStack(Context context) {
        Intent[] intents = new Intent[1];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
        return intents;
    }
}
