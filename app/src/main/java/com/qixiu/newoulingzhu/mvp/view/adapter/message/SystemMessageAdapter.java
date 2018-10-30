package com.qixiu.newoulingzhu.mvp.view.adapter.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qixiu.newoulingzhu.beans.messge.SystemMessageListBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class SystemMessageAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_system_message;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new SystemMessageHolder(itemView, context, this);
    }

    public class SystemMessageHolder extends RecyclerBaseHolder {


        private final TextView mTv_system_title;
        private final TextView mTv_addtime;
        private final TextView mTv_system_content;

        public SystemMessageHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            mTv_system_title = (TextView) itemView.findViewById(R.id.tv_system_title);
            mTv_addtime = (TextView) itemView.findViewById(R.id.tv_addtime);
            mTv_system_content = (TextView) itemView.findViewById(R.id.tv_system_content);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof SystemMessageListBean.SystemMessageListInfo.ListBean) {
                SystemMessageListBean.SystemMessageListInfo.ListBean listBean = (SystemMessageListBean.SystemMessageListInfo.ListBean) mData;
                mTv_system_title.setText(listBean.getTitle());
                mTv_addtime.setText(listBean.getAddtime());
                mTv_system_content.setText(listBean.getMage());

            }
        }
    }
}
