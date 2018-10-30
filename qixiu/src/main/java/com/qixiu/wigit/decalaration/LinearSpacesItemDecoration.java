package com.qixiu.wigit.decalaration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class LinearSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int horizontal;

    public LinearSpacesItemDecoration(int horizontal, int space) {
        this.space = space;
        this.horizontal = horizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = 0;

        if (horizontal == LinearLayoutManager.HORIZONTAL) {
            outRect.right = space;
        } else {
            outRect.right = 0;
        }
        if (horizontal == LinearLayoutManager.VERTICAL) {
            outRect.bottom = space;
        } else {
            outRect.bottom = 0;
        }

        outRect.top = 0;

    }
}
