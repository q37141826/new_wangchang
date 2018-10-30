package com.qixiu.recyclerview_lib;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 高玉恒 on 2017/4/10 0010.
 */

public interface OnRecyclerItemListener<T> {

    public void onItemClick(View v, RecyclerView.Adapter adapter, T data);


}
