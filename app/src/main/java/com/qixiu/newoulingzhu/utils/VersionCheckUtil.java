package com.qixiu.newoulingzhu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.gson.Gson;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.beans.VersionBean;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.service.DownloadService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by HuiHeZe on 2017/9/26.
 */

public class VersionCheckUtil {
    private static final String KEY_DOWNLOAD_URL = "DOWNLOAD_URL";
    private static final String KEY_FILE_PATH = "FILE_PATH";
    private static IsNewVerSion listenner;

    public static void checkVersion(Context context, final Activity activity, IsNewVerSion listenner) {
        OkHttpUtils.get().url(ConstantUrl.versionUrl)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    VersionBean bean = new Gson().fromJson(s, VersionBean.class);
                    if (!bean.getO().getName().equals(ArshowContextUtil.getVersionName(activity))) {
                        setDialog("检测到新版本", bean.getO(), activity, bean.getO().getType());
                        listenner.call(false);
                    } else {
                        listenner.call(true);
                    }
                } catch (Exception e) {
                }
            }
        });


    }

    public static void setCall(IsNewVerSion call) {
        listenner = call;
    }

    public static void setDialog(String message, final VersionBean.OBean oBean, final Activity activity, String version_code) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(activity);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        if (version_code.equals("1")) {
            builder.setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AppManager.getAppManager().finishAllActivity();
                }
            });
        } else {
            builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        }

        builder.setPositiveButton("下载更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //

                try {
                    dialog.dismiss();
                    Intent intent = new Intent(activity.getApplicationContext(), DownloadService.class);
                    intent.putExtra("apkVersion", oBean);
                    activity.getApplicationContext().startService(intent);
//                    Intent intent =new Intent(activity.getApplicationContext(), DownloadService.class);
//                    intent.putExtra(KEY_DOWNLOAD_URL, oBean.getUrl());
//                    intent.putExtra(KEY_FILE_PATH, PictureCut.creatPath()+"/update.apk");
//                    activity.getApplicationContext().startService(intent);

                } catch (Exception e) {
                }
            }
        });

        builder.setMessage(message);
        builder.show();
    }

    public interface IsNewVerSion {
        void call(boolean isNew);
    }

}
