package com.qixiu.adapter;

import android.support.annotation.Keep;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Long on 2018/4/23
 */
@Keep
public final class ItemDecorations {

   public interface ItemDecorationFactory {
        RecyclerView.ItemDecoration create(RecyclerView view);
    }

    public static ItemDecorationFactory vertical(){
        return view -> new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
    }

    public static ItemDecorationFactory horizontal(){
        return view -> new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL);
    }


}
