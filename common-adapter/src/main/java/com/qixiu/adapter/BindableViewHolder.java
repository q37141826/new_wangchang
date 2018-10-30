package com.qixiu.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Long on 2018/4/20 0020
 */
public class BindableViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mBinding;

    BindableViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }
}
