package com.qixiu.newoulingzhu.mvp.view.adapter.homne.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.beans.home.template.TemplateListBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class WritTemplateAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_writ_template;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new WritTemplateHolder(itemView, context, this);
    }

    public class WritTemplateHolder extends RecyclerBaseHolder {


        private final ImageView mIv_template_img;
        private final TextView mTv_template_title;

        public WritTemplateHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mIv_template_img = (ImageView) itemView.findViewById(R.id.iv_template_img);
            mTv_template_title = (TextView) itemView.findViewById(R.id.tv_template_title);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof TemplateListBean.TemplateListInfo.ListBean) {

                TemplateListBean.TemplateListInfo.ListBean listBean = (TemplateListBean.TemplateListInfo.ListBean) mData;
                Glide.with(itemView.getContext()).load(listBean.getSmeta()).skipMemoryCache(false).centerCrop().into(mIv_template_img);
                mTv_template_title.setText(listBean.getTitle());
            }
        }
    }
}
