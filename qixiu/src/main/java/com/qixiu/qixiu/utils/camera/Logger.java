package com.qixiu.qixiu.utils.camera;

import android.util.Log;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     Log工具类，可动态开关
 * </pre>
 */
public final class Logger {

    private static volatile boolean DEBUG = true;

    public static void enable(boolean enable) {
        DEBUG = enable;
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        Log.d(tag, message);
    }

}
