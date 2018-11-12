package com.qixiu.newoulingzhu.mvp.view.activity.guidepage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.mvp.view.activity.LoginActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.wanchang.R;

import me.leolin.shortcutbadger.ShortcutBadger;


public class StartPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        onInitData();
        EMClient.getInstance().groupManager().loadAllGroups();//加载聊天记录
        EMClient.getInstance().chatManager().loadAllConversations();
    }


    protected void onInitData() {
        //// TODO: 2017/8/2 清空app角标，后续可以改到具体位置
        Preference.put(ConstantString.broadCastMessageCount, 0);
        BaseApplication.NOTICE_NUM = 0;
        ShortcutBadger.applyCount(this, 0);

        try {
            if (!Preference.getBoolean(ConstantString.IS_FIRST_LOGIN)) {
                GuidePageActivity.start(this);
                Preference.putBoolean(ConstantString.IS_FIRST_LOGIN, true);
                StartPageActivity.this.finish();
                return;
            }
        } catch (Exception e) {
        }
        try {
            if (TextUtils.isEmpty(Preference.get(ConstantString.USERID, ""))) {
                handeler.postDelayed(new MyRunnable(1), 1000);
            } else {
                handeler.postDelayed(new MyRunnable(2), 1000);
            }
        } catch (Exception e) {
        }
    }


    //跳转的延迟线程
    Handler handeler = new Handler();

    class MyRunnable implements Runnable {
        private int type;

        public MyRunnable(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            if (type == 1) {
                LoginActivity.start(StartPageActivity.this);
            } else {
                MainActivity.start(StartPageActivity.this);
            }
            StartPageActivity.this.finish();
        }
    }
}
