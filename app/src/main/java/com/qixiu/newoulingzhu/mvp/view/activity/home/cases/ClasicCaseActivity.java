package com.qixiu.newoulingzhu.mvp.view.activity.home.cases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.qixiu.newoulingzhu.beans.home.cases.CasesMenuBean;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.MenuActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.cases.CasesFragment;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.hviewpager.HackyViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ClasicCaseActivity extends MenuActivity {

    public static final String TITLE_NAME_CASE = "经典案例";
    public static final String TITLE_NAME_CONSULTING = "最新资讯";

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.view_pager)
    HackyViewPager viewPager;
    private String title;

    public static void start(Context context, Intent intent) {
        context.startActivity(intent);
    }

    @Override
    protected void onInitData() {
        String url = getIntent().getStringExtra(IntentDataKeyConstant.URL);
        Map<String, String> map = new HashMap<>();
        get(url, map, new CasesMenuBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_clasic_case;
    }

    @Override
    protected void onInitViewNew() {
        title = getIntent().getStringExtra(IntentDataKeyConstant.TITLE);
        mTitleView.setTitle(title);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof CasesMenuBean) {
            CasesMenuBean bean = (CasesMenuBean) data;
            List<String> titles = new ArrayList<>();
            List<BaseFragment> fragments = new ArrayList<>();
            for (int j = 0; j < bean.getO().size(); j++) {
                titles.add(bean.getO().get(j).getName());
                CasesFragment fragment = new CasesFragment();
                fragment.setCurrentPage(j);
                fragment.setItemTitle(title + "详情");
                fragment.setType(bean.getO().get(j).getType());
                fragment.setId(bean.getO().get(j).getId());
                fragments.add(fragment);
            }
            initFragment(fragments, titles, tablayout, viewPager);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
            super.onFailure(c_codeBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
