package com.qixiu.newoulingzhu.mvp.view.adapter.homne.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qixiu.newoulingzhu.beans.home.template.TemplateTypeBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class WritTemplateTagAdapter extends RecyclerBaseAdapter {

    public int getAllDataCount() {
        return allDataCount;
    }

    public void setAllDataCount(int allDataCount) {
        this.allDataCount = allDataCount;
    }

    private int allDataCount;

    @Override
    public int getLayoutId() {
        return R.layout.item_template_tag;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new WritTemplateTagHolder(itemView, context, this);
    }



    @Override

    public void onBindViewHolder(RecyclerBaseHolder holder, int position) {
        if (position == getDatas().size()) {
            holder.bindHolder(position);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public class WritTemplateTagHolder extends RecyclerBaseHolder {


        private final RecyclerBaseAdapter mRecyclerBaseAdapter;
        private final TextView mTv_item_search;

        public WritTemplateTagHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mRecyclerBaseAdapter = (RecyclerBaseAdapter) adapter;
            mTv_item_search = (TextView) itemView.findViewById(R.id.tv_item_search);
        }

        @Override
        public void bindHolder(int position) {


            if (position == ((RecyclerBaseAdapter) mAdapter).getDatas().size()) {// 添加末尾
                mTv_item_search.setVisibility(getAllDataCount() <= 7 ? View.GONE : View.VISIBLE);
                if (mRecyclerBaseAdapter.getDatas().size() > 7) {
                    mTv_item_search.setText("收起");
                } else {
                    mTv_item_search.setText("更多");
                }

            } else {// 添加图片

                if (mTv_item_search.getVisibility() == View.GONE) {
                    mTv_item_search.setVisibility(View.VISIBLE);
                }
                if (mData instanceof TemplateTypeBean.TemplateTypeInfo) {
                    TemplateTypeBean.TemplateTypeInfo templateTypeInfo = (TemplateTypeBean.TemplateTypeInfo) mData;
                    if(templateTypeInfo.is_selected()){
                        //todo 设置箭头背景
                    }else {
                    }
                    mTv_item_search.setText(templateTypeInfo.getValue());
                }
            }
        }


    }
}
