package com.qixiu.newoulingzhu.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;

import java.util.Stack;

/**
 * App管理
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     *
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();

        }
        activityStack.add(activity);

    }

    public int getActivitySize() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }

    /**
     *
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() > 0) {
            activity = activityStack.lastElement();
        }

        return activity;
    }

    /**
     * 结束
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);

        }
    }

    /**
     * Activity
     */
    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
            }
        }
//        for (Activity activity : activityStack) {
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有的Activity
     */
    public void finishMainOtherActivity() {
        Stack<Activity> activities = new Stack<>();

        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if (!(activityStack.get(i) instanceof MainActivity)) {
                    activityStack.get(i).finish();
                    activities.add(activityStack.get(i));
                } else {
                    MainActivity activity = (MainActivity) activityStack.get(i);
                    //   activity.setAction(IntentDataKeyConstant.CONSTRAINT_LOGIN_ACTION);
                }
            }
        }
        activityStack.removeAll(activities);
        activities.clear();
    }


    /**
     * 支付成功后关闭确认订单界面及支付界面
     */
//    public void finishPayActivity() {
//        Stack<Activity> activities = new Stack<>();
//
//        for (int i = 0, size = activityStack.size(); i < size; i++) {
//            if (null != activityStack.get(i)) {
//                if (activityStack.get(i) instanceof SelectPayMethoedActivity || activityStack.get(i) instanceof ConfirmOrderActivity) {
//                    activities.add(activityStack.get(i));
//                    activityStack.get(i).finish();
//                }
//            }
//        }
//        activityStack.removeAll(activities);
//        activities.clear();
//    }


    /**
     * 结束进程
     */
    public void AppExit(Context context) {
        restartApp(context);
        System.exit(0);
    }

    /**
     * 重启APP
     *
     * @param context
     */
    public void restartApp(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());

        } catch (Exception e) {
        }
    }
}