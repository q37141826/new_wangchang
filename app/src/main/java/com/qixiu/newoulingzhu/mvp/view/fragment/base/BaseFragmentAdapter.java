package com.qixiu.newoulingzhu.mvp.view.fragment.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private List<? extends BaseFragment> fragments;
    private List<String> tabTitle;
    private FragmentManager fm;

    public BaseFragmentAdapter(FragmentManager fm, List<? extends BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fm=fm;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle == null ? null : tabTitle.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public void setPageTitle(List<String> tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

}