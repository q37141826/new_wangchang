package com.qixiu.adapter;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.qixiu.adapter.databinding.BindDefaultFooterBinding;

/**
 * Created by Long on 2018/4/20
 */
public abstract class BindableFooter extends BindableItem {

    protected abstract void onBindLoadState(ViewDataBinding binding, @BindableRecyclerAdapter.LoadState int state);

    public static BindableFooter createFooter() {
        return new DefaultFooter();
    }

    public static class DefaultFooter extends BindableFooter {

        @Override
        public int getViewType() {
            return R.layout.bind_default_footer;
        }

        @Override
        protected void onBindLoadState(ViewDataBinding binding, @BindableRecyclerAdapter.LoadState int state) {
            BindDefaultFooterBinding footerBinding = (BindDefaultFooterBinding) binding;
            footerBinding.indicator.setVisibility(state == BindableRecyclerAdapter.LoadState.LOADING ? View.VISIBLE : View.GONE);
            footerBinding.text.setVisibility(state == BindableRecyclerAdapter.LoadState.LOADING ? View.GONE : View.VISIBLE);
            switch (state) {
                case BindableRecyclerAdapter.LoadState.NORMAL:
                    footerBinding.text.setText(R.string.bind_load_more);
                    break;
                case BindableRecyclerAdapter.LoadState.FAIL:
                    footerBinding.text.setText(R.string.bind_load_fail);
                    break;
                case BindableRecyclerAdapter.LoadState.COMPLETED:
                    footerBinding.layout.setEnabled(false);
                    footerBinding.text.setText(R.string.bind_load_completed);
                    break;
                case BindableRecyclerAdapter.LoadState.LOADING:
                    footerBinding.layout.setEnabled(false);
                default:
                    break;
            }
        }
    }

}
