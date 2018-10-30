package com.qixiu.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Long on 2018/4/20 0020
 */
public class BindableListAdapter extends BaseAdapter {

    private final List<BindableItem> mItems = new ArrayList<>();

    private ItemActionHandler<?> itemActionHandler;

    public BindableListAdapter(@Nullable List<? extends BindableItem> items) {
        if (items != null) {
            mItems.addAll(items);
        }
    }

    public void setItemActionHandler(ItemActionHandler itemActionHandler) {
        this.itemActionHandler = itemActionHandler;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getViewType();
    }

    @Override
    public BindableItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BindableItem item = getItem(position);
        BindableViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(item.getViewType(), parent, false);
            holder = new BindableViewHolder(Objects.requireNonNull(DataBindingUtil.bind(convertView)));
            convertView.setTag(holder);
        } else {
            holder = (BindableViewHolder) convertView.getTag();
        }
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.item, item);
        if (itemActionHandler != null) {
            binding.setVariable(BR.actionHandler, itemActionHandler);
        }
        binding.executePendingBindings();
        return convertView;
    }

    public void onRest(@Nullable List<? extends BindableItem> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void addAll(@Nullable List<? extends BindableItem> items) {
        if (items != null) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public List<BindableItem> getItems() {
        return mItems;
    }


}
