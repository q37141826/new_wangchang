package com.qixiu.newoulingzhu.engine;

import android.os.Handler;
import android.os.Message;

import com.qixiu.newoulingzhu.engine.bean.UserInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class PlatformLoginEngine implements Handler.Callback {

    private final Handler mHandler;
    private PlatformResultListener mPlatformResultListener;
    private static final int MSG_SMSSDK_CALLBACK = 1;
    private static final int MSG_AUTH_CANCEL = 2;
    private static final int MSG_AUTH_ERROR = 3;
    private static final int MSG_AUTH_COMPLETE = 4;
    public PlatformLoginEngine(PlatformResultListener platformResultListener) {

        this.mPlatformResultListener = platformResultListener;
        mHandler = new Handler(this);

    }

    /**
     * 传对应平台的名字
     *
     * @param platformName
     */
    public void startAuthorize(String platformName) {
        Platform platform = ShareSDK.getPlatform(platformName);
        if (platform == null) return;
        platform.setPlatformActionListener(new PlatformLoginListener(this));
        // 关闭SSO授权
        platform.SSOSetting(false);
        platform.removeAccount(true);
        platform.showUser(null);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL:

                // 取消授权
                if (mPlatformResultListener != null) mPlatformResultListener.onCancel();

                break;
            case MSG_AUTH_ERROR:

                // 授权失败
                if (mPlatformResultListener != null) mPlatformResultListener.onFailure();

                break;
            case MSG_AUTH_COMPLETE:
                // 授权成功 封装UserInfo
                if (msg.obj != null) {
                    List<UserInfo> userInfoList = (List<UserInfo>) msg.obj;
                    if (mPlatformResultListener != null)
                        mPlatformResultListener.onSuccess(userInfoList.get(0));
                } else {
                    mPlatformResultListener.onFailure();
                }

                break;
            case MSG_SMSSDK_CALLBACK:
                break;

        }
        return false;
    }

    public static class PlatformLoginListener implements PlatformActionListener {

        private final WeakReference<PlatformLoginEngine> mWeakReference;

        public PlatformLoginListener(PlatformLoginEngine platformLoginEngine) {
            mWeakReference = new WeakReference<>(platformLoginEngine);
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            PlatformLoginEngine platformLoginEngine = mWeakReference.get();
            if (platformLoginEngine == null) return;
            if (i == Platform.ACTION_USER_INFOR) {
                String name = platform.getName();
                int type = 0;
                if (QQ.NAME.equals(name)) {
                    type = 1;
                } else if (Wechat.NAME.equals(name)) {
                    type = 2;
                } else if (SinaWeibo.NAME.equals(name)) {
                    type = 3;
                }
                Message msg = new Message();
                msg.what = MSG_AUTH_COMPLETE;
                List<UserInfo> userInfoList = new ArrayList<>();
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(platform.getDb().getUserId());
                userInfo.setUserName(platform.getDb().getUserName());
                userInfo.setType(type);
                userInfo.setUserIcon(platform.getDb().getUserIcon());
                userInfoList.add(userInfo);
                msg.obj = userInfoList;
                platformLoginEngine.mHandler.sendMessage(msg);
                platform.removeAccount();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            PlatformLoginEngine platformLoginEngine = mWeakReference.get();
            if (platformLoginEngine == null) return;
            if (i == Platform.ACTION_USER_INFOR) {
                platformLoginEngine.mHandler.sendEmptyMessage(MSG_AUTH_ERROR);
                platform.removeAccount();
            }
            throwable.printStackTrace();
        }

        @Override
        public void onCancel(Platform platform, int i) {
            PlatformLoginEngine platformLoginEngine = mWeakReference.get();
            if (platformLoginEngine == null) return;
            if (i == Platform.ACTION_USER_INFOR) {
                platformLoginEngine.mHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
                platform.removeAccount();
            }
        }

    }

    public interface PlatformResultListener {
        void onCancel();

        void onFailure();

        void onSuccess(UserInfo userInfo);

    }


}
