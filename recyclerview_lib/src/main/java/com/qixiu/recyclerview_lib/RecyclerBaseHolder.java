package com.qixiu.recyclerview_lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by 高玉恒 on 2017/4/10 0010.
 * 仅供RecyclerView使用的Holder
 */

public abstract class RecyclerBaseHolder<D> extends RecyclerView.ViewHolder {
    protected Context mContext;
    protected RecyclerView.Adapter mAdapter;
    protected D mData;

    private RecyclerBaseAdapter.ClickListenner<D> clickListenner;
    private RecyclerBaseAdapter.ClickListenner2<D> clickListenner2;


    public RecyclerBaseHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
        super(itemView);
        this.mContext = context;
        this.mAdapter = adapter;
    }

    public void setData(D data) {
        this.mData = data;
        this.itemView.setTag(data);
    }

    public abstract void bindHolder(int position);


    public D getData() {

        return this.mData;
    }

    public RecyclerBaseAdapter.ClickListenner<D> getClickListenner() {
        return clickListenner;
    }

    public void setClickListenner(RecyclerBaseAdapter.ClickListenner<D> clickListenner) {
        this.clickListenner = clickListenner;
    }


    public RecyclerBaseAdapter.ClickListenner2<D> getClickListenner2() {
        return clickListenner2;
    }

    public void setClickListenner2(RecyclerBaseAdapter.ClickListenner2<D> clickListenner) {
        this.clickListenner2 = clickListenner;
    }

}
