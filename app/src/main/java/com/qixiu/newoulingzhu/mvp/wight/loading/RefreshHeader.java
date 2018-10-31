package com.qixiu.newoulingzhu.mvp.wight.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.adapter.GlideCacheUtil;
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.SimpleViewSwitcher;
import com.qixiu.wanchang.R;

/**
 * Created by my on 2018/9/26.
 */

public class RefreshHeader extends ArrowRefreshHeader {
    Context context;
    private ImageView imageview;
    private SimpleViewSwitcher progressBar;

    public RefreshHeader(Context context) {
        super(context);
        this.context=context;
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    public void setArrowImageView(int resid) {
//        super.setArrowImageView(resid);
        imageview = (ImageView)findViewById(R.id.listview_header_arrow);
        progressBar = (SimpleViewSwitcher)findViewById(com.jcodecraeer.xrecyclerview.R.id.listview_header_progressbar);
        GlideCacheUtil.getInstance().clearImageAllCache(getContext());
        Glide.with(context).load(resid).into(imageview);

    }

    @Override
    public void setState(int state) {
        super.setState(state);
        imageview.setAnimation(null);
        imageview.setVisibility(VISIBLE);
        progressBar.setVisibility(INVISIBLE);
    }
}
