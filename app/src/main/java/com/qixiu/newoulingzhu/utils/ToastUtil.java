package com.qixiu.newoulingzhu.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.qixiu.newoulingzhu.application.BaseApplication;

public class ToastUtil {

    private static String cacheToastStr;
    private static long cacheToastTime;
    private static final boolean DEBUG = true;

    /**
     * 显示toast
     *
     * @param context 上下文
     * @param resId   显示内容的资源id
     */
    public static void showToast(Context context, int resId) {
        if (checkNull(context)) return;
        showToast(context, context.getResources().getString(resId));
    }

    public static void testToast(Context context, int resId) {
        if (DEBUG) {
            if (checkNull(context)) return;
            showToast(context, context.getResources().getString(resId));
        }
    }

    public static void toast(String content) {

        showToast(BaseApplication.getApp(), content);
    }

    public static void toast(int contentId) {

        showToast(BaseApplication.getApp(), contentId);
    }

    /**
     * 显示toast
     *
     * @param context 上下文
     * @param content 显示内容
     */
    public static void showToast(Context context, String content) {
        if (checkNull(context)) return;
        //如果内容和上次显示的内容相同并且在1秒钟内则不显示
        final long time = System.currentTimeMillis();
        if (content.equals(cacheToastStr) && (time - cacheToastTime) < 1000) {
            return;
        }
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        cacheToastStr = content;
        cacheToastTime = time;
    }

    public static void testToast(Context context, String content) {
        if (DEBUG) {
            if (checkNull(context)) return;
            //如果内容和上次显示的内容相同并且在1秒钟内则不显示
            final long time = System.currentTimeMillis();
            if (content.equals(cacheToastStr) && (time - cacheToastTime) < 1000) {
                return;
            }
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            cacheToastStr = content;
            cacheToastTime = time;
        }
    }


    private static boolean checkNull(Context context) {
        //上下文为空或者activity已经finish则返回true
        return (context == null) || (context instanceof Activity && ((Activity) context).isFinishing());
    }
}