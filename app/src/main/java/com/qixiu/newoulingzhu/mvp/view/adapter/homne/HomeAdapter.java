package com.qixiu.newoulingzhu.mvp.view.adapter.homne;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

/**
 * Created by my on 2018/8/13.
 */

public class HomeAdapter extends RecyclerBaseAdapter {


    @Override
    public int getLayoutId() {
        return R.layout.item_home;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new HomeHolder(itemView,context,this);
    }

    public class HomeHolder extends RecyclerBaseHolder {

        public HomeHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
        }

        @Override
        public void bindHolder(int position) {

        }
    }
}
