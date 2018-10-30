package com.qixiu.qixiu.titleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseView {
    protected Context mContext;
    protected View mView;
    protected Object mTag;

    public BaseView(Context context) {
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(getLayoutID(), null);
//        ButterKnife.inject(this, mView);
        initView();
    }

    public View getView() {
        return mView;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    protected abstract int getLayoutID();

    protected abstract void initView();


}
