package com.qixiu.newoulingzhu.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.beans.VersionBean;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.utils.MyFileProvider;
import com.qixiu.wanchang.R;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.Executor;

/**
 * 注释：通知栏下载的服务
 */
public class UpdateVersionService extends IntentService
        implements Callback.ProgressCallback<File>, Callback.CommonCallback<File> {
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    private int notifyId = 102;
    private final Executor executor = new PriorityExecutor(3, true);
    private VersionBean.OBean apkVersion;
    private AlertDialog mAlertDialog;
    private TextView mMTv_hint;
    private String mProgress;


    public UpdateVersionService() {
        super(UpdateVersionService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initNotify();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            apkVersion = intent.getParcelableExtra("apkVersion");
            if (apkVersion != null) {
                apkVersion.setAppName(apkVersion.getUrl().substring(apkVersion.getUrl().lastIndexOf("/") + 1));
                apkVersion.setSavePath(ArshowContextUtil.getResourcePath() + File.separator + apkVersion.getAppName());
                RequestParams params = new RequestParams(apkVersion.getUrl());
                params.setAutoResume(true);
                params.setAutoRename(true);
                params.setSaveFilePath(apkVersion.getSavePath());
                params.setExecutor(executor);
                params.setCancelFast(true);
                x.http().get(params, this);
            }
        }
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        //初始化通知栏
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(PendingIntent.getActivity(this, 1, new Intent(),
                        PendingIntent.FLAG_CANCEL_CURRENT)).setOngoing(
                true)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.mipmap.logo);
    }

    /**
     * 安装下载的apk
     */
    @Override
    public void onSuccess(File result) {
        ArshowContextUtil.installApk(apkVersion.getSavePath());
        install(BaseApplication.getContext());
    }
    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public  void install(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = MyFileProvider.getUriForFile(context,context.getString(R.string.provider) , new File( apkVersion.getSavePath(),apkVersion.getAppName()));
        } else {
            uri = Uri.fromFile(new File( apkVersion.getSavePath(),apkVersion.getAppName()));
        }
        intent.setDataAndType(uri ,
                "application/vnd.android.package-archive");// //new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "myApp.apk")
        context.startActivity(intent);
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //  mBuilder.setContentTitle("下载失败，请重试");// 通知首次出现在通知栏，带上升动画效果的
        Toast.makeText(this.getApplication(), "下载失败，请重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(CancelledException cex) {
    }

    @Override
    public void onFinished() {
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }

    }

    @Override
    public void onWaiting() {
    }

    @Override
    public void onStarted() {
        Toast.makeText(this.getApplication(), "开始下载", Toast.LENGTH_SHORT).show();
        mAlertDialog = showProgressDialog("App下载进度: 0%");
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        mProgress = new DecimalFormat("#.0").format((current * 1.0 / total) * 100) + "%";
        mMTv_hint.setText("App下载进度: " + mProgress);
//        mBuilder.setContentTitle(apkVersion.getAppName()).setContentText("进度：" + progress).setTicker(
//                "开始下载");// 通知首次出现在通知栏，带上升动画效果的
//        //确定进度的
//        mBuilder.setProgress((int) total, (int) current, false); // 这个方法是显示进度条  设置为true就是不确定的那种进度条效果
//        mNotificationManager.notify(notifyId, mBuilder.build());

    }

    public AlertDialog showProgressDialog(String message) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if (activity == null) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = View.inflate(this, R.layout.dialog_loading, null);
        builder.setView(view);

        builder.setCancelable(false);
        ProgressBar pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        mMTv_hint = (TextView) view.findViewById(R.id.tv_hint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb_loading.setIndeterminateTintList(ContextCompat.getColorStateList(this, R.color.blue));
        }
        mMTv_hint.setText(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }

}
