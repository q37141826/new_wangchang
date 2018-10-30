package com.qixiu.newoulingzhu.mvp.view.activity.base;

import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.easeui.utils.StatusBarUtils;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.titleview.TitleView;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/6/14 0014.
 */

public abstract class TitleActivity extends BaseActivity implements View.OnClickListener {

    protected TitleView mTitleView;

    /**
     * 在这里添加公共的标题
     */
    @Override
    protected void onInitView() {
        mTitleView = new TitleView(this);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.vg_title);
        if (viewGroup != null) {
            viewGroup.addView(mTitleView.getView());

        } else {
            ToastUtil.toast(R.string.main_notfindTitle);
        }
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adustTitle();
        onInitViewNew();
    }

    public void adustTitle() {
        setLayout(StatusBarUtils.getStatusBarHeight(getContext()));
    }

    protected void setLayout(int height) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTitleView.getView().getLayoutParams();
        params.topMargin =height;
//        mTitleView.getView().setY( StatusBarUtils.getStatusBarHeight(getContext()));
        mTitleView.getView().setLayoutParams(params);
//        mTitleView.getView().setY( StatusBarUtils.getStatusBarHeight(getContext()));
    }


    protected abstract void onInitViewNew();


}
