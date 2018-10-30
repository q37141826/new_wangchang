package com.qixiu.newoulingzhu.mvp.view.adapter.mine.member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.beans.mine_bean.member.DredgeMemberBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class DredgeMemberAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_member_type;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new DredgeMemberHolder(itemView, context, this);
    }

    public class DredgeMemberHolder extends RecyclerBaseHolder {


        private final TextView mTv_member_name;
        private final ImageView mIv_select_icon;

        public DredgeMemberHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mTv_member_name = (TextView) itemView.findViewById(R.id.tv_member_name);
            mIv_select_icon = (ImageView) itemView.findViewById(R.id.iv_select_icon);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof DredgeMemberBean.DredgeMemberInfo) {
                DredgeMemberBean.DredgeMemberInfo dredgeMemberInfo = (DredgeMemberBean.DredgeMemberInfo) mData;
                mTv_member_name.setText(dredgeMemberInfo.getName());
                if (dredgeMemberInfo.isSelected()) {
                    Glide.with(mContext).load(R.mipmap.member_select).skipMemoryCache(false).into(mIv_select_icon);
                } else {
                    Glide.with(mContext).load(R.mipmap.member_not_select).skipMemoryCache(false).into(mIv_select_icon);
                }
            }

        }
    }

}
