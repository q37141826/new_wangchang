package com.qixiu.adapter;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Long on 2018/4/23 0023
 */
public final class BindingRecyclerViewAdapters {

    @BindingAdapter(value = {"items", "itemActionHandler"}, requireAll = false)
    public static <T extends BindableItem> void bindItems(RecyclerView recyclerView,
                                                          @Nullable List<T> items,
                                                          @Nullable ItemActionHandler<T> actionHandler) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null && items != null) {
            BindableRecyclerAdapter bindingAdapter = new BindableRecyclerAdapter(items);
            if (actionHandler != null) {
                bindingAdapter.setItemActionHandler(actionHandler);
            }
            recyclerView.setAdapter(bindingAdapter);
        }
    }

    @BindingAdapter("layoutManager")
    public static void bindLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory factory) {
        recyclerView.setLayoutManager(factory.create(recyclerView));
    }

    @BindingAdapter("divider")
    public static void bindDivider(RecyclerView view, ItemDecorations.ItemDecorationFactory factory) {
        view.addItemDecoration(factory.create(view));
    }

}
