package com.qixiu.newoulingzhu.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.qixiu.newoulingzhu.application.BaseApplication;


/**
 * @author audiebant
 */
public class Preference {

    private static final String TAG = "Preference";

    public static void put(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * 获取一个boolean类型的数据,默认值为false
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        boolean result = sp.getBoolean(key, false);
        return result;
    }
    public static void putBoolean(String key, boolean b){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, b);
        edit.apply();
    }
    /**
     * 获取一个long类型的数据,默认值为0
     * @param context
     * @param key
     * @return
     */
    public static long getLong(String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        long result = sp.getLong(key, 0);
        return result;
    }
    public static void putLong(String key, long b){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, b);
        edit.apply();
    }
    public static void put(String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }



    public static String get(String key, String def) {
        String value = def;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (sp.contains(key)) {
            value = sp.getString(key, def);
        }
        return value;
    }

    public static int get(String key, int def) {
        int value = def;
        Log.i(TAG, "get: context : "+ BaseApplication.getContext());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (sp.contains(key)) {
            value = sp.getInt(key, def);
        }
        return value;
    }

    public static void setFlag(String flagName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(flagName, 1);
        edit.apply();
    }

    public static void clearFlag(String flagName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(flagName, 0);
        edit.apply();
    }

    public static void clearAllFlag() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        SharedPreferences.Editor edit = sp.edit();
   /* //BaseApplication.locationService.stop();
    String  lat=sp.getString(Constans.LAT,"0");
    String  lng=sp.getString(Constans.LNG,"0");*/
        edit.clear();
        edit.apply();
    /*edit.putString(Constans.LAT,lat);
    edit.putString(Constans.LNG,lng);
    edit.putBoolean(Constans.COME_IN,true);*/
        edit.apply();
    }

    public static Boolean isFlag(String flagName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getContext());
        if (sp.contains(flagName) && sp.getInt(flagName, 0) == 1) {
            return true;
        }
        return false;
    }

}