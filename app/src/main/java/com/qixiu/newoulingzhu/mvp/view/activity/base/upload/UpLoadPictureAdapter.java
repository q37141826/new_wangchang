package com.qixiu.newoulingzhu.mvp.view.activity.base.upload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qixiu.recyclerview_lib.AndroidUtils;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class UpLoadPictureAdapter extends RecyclerBaseAdapter {

    private int width_grid;
    private int maxCount = 9;
    private ViewGroup.LayoutParams layoutParams;


    @Override
    public int getLayoutId() {
        return R.layout.item_upload_pictrue;
    }

    public void setMaxPictureCount(int maxCount) {
        if (maxCount <= 0) {
            this.maxCount = 9;
        } else {
            this.maxCount = maxCount;
        }


    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        setTileWith(itemView, context);
        return new UpLoadPictureHolder(itemView, context, this);
    }

    public void setItemLayoutParams(ViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    private void setTileWith(View itemView, Context context) {
        if (layoutParams != null) {
            itemView.setLayoutParams(layoutParams);
        } else {
            if (width_grid == 0) {
                width_grid = (int) ((AndroidUtils.getDeviceWidth(context)));
            }
            int width = width_grid / 4 - AndroidUtils.dip2px(context, 11);
            int height = width;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
            itemView.setLayoutParams(layoutParams);
        }

    }

    @Override

    public void onBindViewHolder(RecyclerBaseHolder holder, int position) {
        if (position == getDatas().size()) {
            holder.bindHolder(position);
        } else {
            super.onBindViewHolder(holder, position);
        }


    }

    private class UpLoadPictureHolder extends RecyclerBaseHolder<String> {


        private final ImageView mIv_picture;
        private final View mRl_item;


        public UpLoadPictureHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mIv_picture = (ImageView) itemView.findViewById(R.id.iv_picture);
            mRl_item = itemView.findViewById(R.id.rl_item);

        }

        @Override
        public void bindHolder(int position) {
            if (position == ((RecyclerBaseAdapter) mAdapter).getDatas().size()) {// 添加末尾
                Glide.with(itemView.getContext()).load(R.mipmap.shizi).skipMemoryCache(false).centerCrop().into(mIv_picture);
                mRl_item.setVisibility(position == maxCount ? View.GONE : View.VISIBLE);

            } else {// 添加图片
                if (mRl_item.getVisibility() == View.GONE) {
                    mRl_item.setVisibility(View.VISIBLE);
                }
                Glide.with(itemView.getContext()).load(mData).skipMemoryCache(false).into(mIv_picture);
            }
        }
    }
}
