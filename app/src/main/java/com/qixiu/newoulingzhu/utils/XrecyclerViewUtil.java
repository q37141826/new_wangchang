package com.qixiu.newoulingzhu.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.utils.decoration.SpaceItemsDecoration;

/**
 * Created by my on 2018/7/20.
 */

public class XrecyclerViewUtil {

    public static void setXrecyclerView(XRecyclerView recyclerView, XRecyclerView.LoadingListener listenner, Context context, boolean IS_USE_REFRESH, int declorLineHeight, RecyclerView.LayoutManager manager) {
        recyclerView.setPullRefreshEnabled(IS_USE_REFRESH);
        if (listenner != null) {
            recyclerView.setLoadingListener(listenner);
        }
        recyclerView.addItemDecoration(new SpaceItemsDecoration(declorLineHeight));
        if (manager == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(manager);
        }
    }

}
