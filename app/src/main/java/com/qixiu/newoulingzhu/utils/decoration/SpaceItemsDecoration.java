package com.qixiu.newoulingzhu.utils.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class SpaceItemsDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemsDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;


    }
}
