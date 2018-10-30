package com.qixiu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * RecyclerView 空分割线
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int halfSpace;

    public SpaceItemDecoration(Context context, int space) {
        float density = context.getResources().getDisplayMetrics().density;
        int size = (int) (density * space+ 0.5f);
        this.halfSpace = size / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = halfSpace;
        outRect.bottom = halfSpace;
        outRect.left = halfSpace;
        outRect.right = halfSpace;
    }
}
