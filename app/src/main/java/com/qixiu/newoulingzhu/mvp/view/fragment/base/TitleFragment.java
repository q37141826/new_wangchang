package com.qixiu.newoulingzhu.mvp.view.fragment.base;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.titleview.TitleView;
import com.qixiu.wanchang.R;



/**
 * Created by Administrator on 2017/7/18 0018.
 */

public abstract class TitleFragment extends BaseFragment {

    protected TitleView mTitleView;

    @Override
    protected void onInitView(View view) {
        mTitleView = new TitleView(view.getContext());
        ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.vg_title);
        if (viewGroup != null) {
            viewGroup.addView(mTitleView.getView());

        } else {
            ToastUtil.toast(R.string.main_notfindTitle);
        }
        onInitViewNew(view);
    }

    protected abstract void onInitViewNew(View view);
    public void startActivity(Class T){
        Intent intent=new Intent(getActivity(),T);
        startActivity(intent);
    }
}
