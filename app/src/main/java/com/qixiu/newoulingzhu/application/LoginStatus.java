package com.qixiu.newoulingzhu.application;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.qixiu.newoulingzhu.beans.login.LoginBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.utils.Preference;

/**
 * Created by Long on 2018/4/26
 */
public final class LoginStatus {

    private static LoginStatus instance = new LoginStatus();

    /*是否已登录*/
    private volatile boolean logged;
    private          String  token;



    public static boolean isLogged() {
        return !TextUtils.isEmpty(Preference.get(ConstantString.USERID,""));
    }

    @Nullable
    public static String getToken() {
        return  Preference.get(ConstantString.USERID,"");//测试的时候改变一下这个地方
    }

    /*登录成功*/
    public static void logged(LoginBean loginBean) {
        Preference.put(ConstantString.USERID,loginBean.getO().getId());
        Preference.put(ConstantString.NICK_NAME,loginBean.getO().getTrue_name());
        Preference.put(ConstantString.TRUE_NAME,loginBean.getO().getTrue_name());
        Preference.put(ConstantString.HEAD,loginBean.getO().getHead());
        Preference.put(ConstantString.PHONE,loginBean.getO().getPhone());
        Preference.put(ConstantString.EMAIL,loginBean.getO().getEmlia());
    }

    /*退出登录*/
    public static void logout() {
        instance.logged = false;
        instance.token = null;
        Preference.clearAllFlag();
        Preference.putBoolean(ConstantString.IS_FIRST_LOGIN,true);
    }




}
