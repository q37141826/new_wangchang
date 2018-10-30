package com.qixiu.adapter;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Long on 2018/4/20
 */
public abstract class BindableItem {

    public abstract int getViewType();

    /**
     * @see GridLayoutManager#getSpanCount()
     */
    public int getSpanSize(int spanSize) {
        return spanSize;
    }

}
