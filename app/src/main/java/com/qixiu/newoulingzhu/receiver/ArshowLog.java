package com.qixiu.newoulingzhu.receiver;

import android.util.Log;

import com.qixiu.wanchang.BuildConfig;


/**
 * Created by 秀宝-段誉 on 2016-08-11 16:50.
 * <p>
 * 说明：日志类
 */
public class ArshowLog
{
    /**
     * 发布的时候会自动改为false
     */
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(Class<?> cls, String i)
    {
        if (DEBUG)
            Log.i(cls.getSimpleName(), i);
    }

    public static void d(Class<?> cls, String d)
    {
        if (DEBUG)
            Log.d(cls.getSimpleName(), d);
    }

    public static void w(Class<?> cls, String w)
    {
        if (DEBUG)
            Log.w(cls.getSimpleName(), w);
    }

    public static void e(Class<?> cls, String e)
    {
        if (DEBUG)
            Log.e(cls.getSimpleName(), e);
    }
}
