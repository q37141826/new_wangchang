package com.qixiu.newoulingzhu.mvp.view.fragment.home;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.utils.StatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragmentAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.TitleFragment;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.hviewpager.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.qixiu.newoulingzhu.mvp.view.fragment.home.MessageConsultingFragment.FINISHED;
import static com.qixiu.newoulingzhu.mvp.view.fragment.home.MessageConsultingFragment.ON_THE_WAY;

/**
 * Created by my on 2018/8/13.
 */

public class MessageFragment extends TitleFragment {
    private MessageConsultingFragment.OnUpdataMessageCountListener listener;

    @BindView(R.id.tabMessage)
    TabLayout tabMessage;
    @BindView(R.id.viewPagerMessage)
    HackyViewPager viewPagerMessage;
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private String[] titles = {"处理中", "已完成", "系统消息"};
    private BaseFragmentAdapter baseFragmentAdapter;

    public void setListener(MessageConsultingFragment.OnUpdataMessageCountListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("消息");
        mTitleView.getLeftView().setVisibility(View.GONE);
    }


    @Override
    protected void onInitViewNew(View view) {
        adustTitle();   //调整一下      标题
        initFragments();
    }

    private void adustTitle() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleView.getView().getLayoutParams();
        params.topMargin = StatusBarUtils.getStatusBarHeight(getContext());
        mTitleView.getView().setLayoutParams(params);
    }

    private void initFragments() {
        fragmentList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < this.titles.length; i++) {
            titles.add(this.titles[i]);
        }
        MessageConsultingFragment fragmentGoing = new MessageConsultingFragment();
        fragmentGoing.setType(ON_THE_WAY);
        fragmentGoing.setOnUpdataMessageCountListener(listener);
        MessageConsultingFragment fragmentFinished = new MessageConsultingFragment();
        fragmentFinished.setOnUpdataMessageCountListener(listener);
        fragmentFinished.setType(FINISHED);
        SystemMessageFragment systemMessageFragment = new SystemMessageFragment();
        fragmentList.add(fragmentGoing);
        fragmentList.add(fragmentFinished);
        fragmentList.add(systemMessageFragment);
        baseFragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), fragmentList);//注意这个地方  fragmentmanager  不要用getSurrpotFragmentmanager
        baseFragmentAdapter.setPageTitle(titles);
        viewPagerMessage.setAdapter(baseFragmentAdapter);
        viewPagerMessage.setOffscreenPageLimit(3);
        tabMessage.setupWithViewPager(viewPagerMessage);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message;
    }

    public void onMsgReceiveRefresh() {
        for (int i = 0; i <fragmentList.size() ; i++) {
            if(fragmentList.get(i) instanceof XRecyclerView.LoadingListener){
                XRecyclerView.LoadingListener listener= (XRecyclerView.LoadingListener) fragmentList.get(i);
                listener.onRefresh();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.onUpdataMessageCount();
    }
}
