package com.qixiu.newoulingzhu.mvp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.application.LoginStatus;
import com.qixiu.newoulingzhu.beans.messge.MessageListBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.BaseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.HomeFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.MessageConsultingFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.MessageFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.MineFragment;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.DrawableUtils;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class MainActivity extends BaseActivity implements View.OnClickListener, MessageConsultingFragment.OnUpdataMessageCountListener, OKHttpUIUpdataListener, RecyclerBaseAdapter.ClickListenner {
    //
    private OKHttpRequestModel okHttpRequestModel;
    public static String MAINMESSAGELIST_FLAG;
    public static int unreadMsgCount = 0;
    @BindView(R.id.textViewHomeBottom)
    TextView textViewHomeBottom;
    @BindView(R.id.textViewPersonBottom)
    TextView textViewPersonBottom;
    @BindView(R.id.imageViewHomeBottom)
    ImageView imageViewHomeBottom;
    @BindView(R.id.textViewMessageBottom)
    TextView textViewMessageBottom;
    @BindView(R.id.fragmen_for_fragment)
    FrameLayout fragmen_for_fragment;
    //底部的导航按钮图形
    private int imagesSelected[] = {R.mipmap.home_index_sel3x, R.mipmap.home_per_center_sel2x};
    private int imagesNotSelected[] = {R.mipmap.home_index_nor3x, R.mipmap.home_per_center_nor2x};
    private List<BaseFragment> fragments = new ArrayList<>();
    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;
    BroadcastReceiver receiver;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }


    @Override
    protected void onInitData() {
        okHttpRequestModel = new OKHttpRequestModel(this);

        setMessageListenner();
    }

    private void setMessageListenner() {
        //改变下面消息数量状态的接收者
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshMessage();
            }
        };
        refreshMessage();
        IntentFilter intentFilter = new IntentFilter(IntentDataKeyConstant.BROADCAST_MESSAGE);
        registerReceiver(receiver, intentFilter);
        if (!TextUtils.isEmpty(MAINMESSAGELIST_FLAG)) {
//            JpushEngine.startActivity(this, SystemMessageActivity.class);
            MAINMESSAGELIST_FLAG = null;
        }
//        VersionCheckUtil.checkVersion(this, this);
        HyEngine.login(this, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING), false, StringConstants.EMPTY_STRING, null);

    }

    public void refreshMessage() {
        if (!(AppManager.getAppManager().currentActivity() instanceof ChatActivity)) {
            if (messageFragment != null && messageFragment.isAdded()) {
                messageFragment.onMsgReceiveRefresh();
            }
            getMessageUread();
        }
    }


    @Override
    protected void onInitView() {
        Preference.putBoolean(ConstantString.IS_GROUP, true);//设置是否为群聊模式
        homeFragment = new HomeFragment();
        homeFragment.setListenner(this);
        messageFragment = new MessageFragment();
        messageFragment.setListener(this);//设置监听
        mineFragment = new MineFragment();
        fragments.add(homeFragment);
        fragments.add(messageFragment);
        fragments.add(mineFragment);
        FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            fragmentTransaction.add(R.id.fragmen_for_fragment, fragments.get(i),
                    fragments.get(i).getClass().getName());
        }
        fragmentTransaction.commit();
        switchFragment(homeFragment, fragmen_for_fragment.getId());

        textViewHomeBottom.setOnClickListener(this);
        textViewPersonBottom.setOnClickListener(this);
        imageViewHomeBottom.setOnClickListener(this);


    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewHomeBottom:
                setDrawble(textViewHomeBottom, imagesSelected[0]);
                switchFragment(homeFragment, fragmen_for_fragment.getId());
                setupBottomBar(0);
                break;
            case R.id.textViewPersonBottom:
                setDrawble(textViewPersonBottom, imagesSelected[1]);
                switchFragment(mineFragment, fragmen_for_fragment.getId());
                setupBottomBar(1);
                break;
            case R.id.imageViewHomeBottom:
                switchFragment(messageFragment, fragmen_for_fragment.getId());
                DrawableUtils.setDrawableResouce(textViewHomeBottom, getContext(), 0, imagesNotSelected[0], 0, 0);
                DrawableUtils.setDrawableResouce(textViewPersonBottom, getContext(), 0, imagesNotSelected[1], 0, 0);
                setupBottomBar(2);
                break;
        }
    }

    private void setDrawble(TextView textView, int resouce) {
        DrawableUtils.setDrawableResouce(textViewHomeBottom, getContext(), 0, imagesNotSelected[0], 0, 0);
        DrawableUtils.setDrawableResouce(textViewPersonBottom, getContext(), 0, imagesNotSelected[1], 0, 0);
        DrawableUtils.setDrawableResouce(textView, getContext(), 0, resouce, 0, 0);
    }


    private void setupBottomBar(int index) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupBottomBar(0);
        getMessageUread();
    }


    int systemMessageCount = 0;

    @Override
    public void onUpdataMessageCount() {
        getMessageUread();
    }


    public void setMessagePoint(String count) {
//        try {
//            systemMessageCount = Integer.parseInt(count);
//        } catch (Exception e) {
//
//        }
        Log.d("LOGCAT", "未读消息数量" + unreadMsgCount);
//        int totalCount = unreadMsgCount + systemMessageCount;
        //减去单聊未读数
        int totalCount = unreadMsgCount - HyEngine.getSingleUnReadMsgCount();
        if (totalCount > 0) {
            textViewMessageBottom.setVisibility(View.VISIBLE);
            if (totalCount > 99) {
                textViewMessageBottom.setText(99 + StringConstants.EMPTY_STRING);
            } else {
                textViewMessageBottom.setText(totalCount + StringConstants.EMPTY_STRING);
            }

        } else {
            textViewMessageBottom.setText(StringConstants.STRING_0);
            textViewMessageBottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof MessageListBean) {
            MessageListBean listBean = (MessageListBean) data;
//            setMessagePoint(listBean.getO().getXt());
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }

    public void getMessageUread() {
        HyEngine.loadAllConversationAndMessage();
        unreadMsgCount = HyEngine.getUnReadMsgCount();
        setMessagePoint(unreadMsgCount+"");
//        getSystemUnread();
    }


    @Override
    public void click(View view, Object o) {
        refreshMessage();
    }

    public void getSystemUnread() {
        Map<String, String> map = new HashMap();
        map.put("uid", LoginStatus.getToken());
        map.put("pageNo", 1 + "");
        map.put("pageSize", 100 + "");
        map.put("doing", 1 + "");
        okHttpRequestModel.okhHttpPost(ConstantUrl.messageList, map, new MessageListBean());
    }
}
