package com.qixiu.newoulingzhu.mvp.view.activity.base;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragmentAdapter;

import java.util.List;

/**
 * Created by my on 2018/8/23.
 */

public abstract class MenuActivity extends RequstActivity {

    public void initFragment(List<BaseFragment> fragments, List<String> titles, TabLayout tablayout,ViewPager viewPager) {
       BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);//注意这个地方  fragmentmanager  在fragment里面不要用getSurrpotFragmentmanager
        adapter.setPageTitle(titles);
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

}
