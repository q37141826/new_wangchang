package com.qixiu.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Long on 2018/4/20
 */
public class BindableRecyclerAdapter extends RecyclerView.Adapter<BindableViewHolder> {
    private static final String TAG = "BindAdapter";
    private static final boolean DEBUG = true;

    /*notify recyclerView refresh*/
    private final Handler            mHandler = new Handler();
    /*all the items*/
    private final List<BindableItem> mItems   = new ArrayList<>();

    /*footer view*/
    private BindableFooter mBindingFooter;

    /*item click listener*/
    private ItemActionHandler<?> mItemActionHandler;
    /*can load more*/
    private boolean mCanLoadMore = false;
    /*page size*/
    private int mPageSize = 10;
    /*load more listener*/
    private OnLoadMoreListener onLoadMoreListener;

    @LoadState
    private int mLoadState = LoadState.NORMAL;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LoadState.NORMAL, LoadState.LOADING, LoadState.FAIL, LoadState.COMPLETED})
    @interface LoadState {
        int NORMAL = 1;
        int LOADING = 2;
        int FAIL = 3;
        int COMPLETED = 4;
    }

    /*监听是否滑动到底部*/
    private final RecyclerView.OnScrollListener onScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!mCanLoadMore || onLoadMoreListener == null ||
                            mLoadState == LoadState.COMPLETED) {
                        //没有开启加载更多，没有设置加载更多事件监听，加载状态为加载完成的条件下
                        //不判断是否需要加载更多
                        return;
                    }

                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int lastVisiblePosition = getLastVisiblePosition(layoutManager);
                    if (lastVisiblePosition == layoutManager.getItemCount() - 1 &&
                            mLoadState == LoadState.NORMAL) {
                        //开始加载更多
                        onLoadMore();
                    }
                }

                private int getLastVisiblePosition(RecyclerView.LayoutManager layoutManager) {
                    int lastVisiblePosition = 0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager =
                                (StaggeredGridLayoutManager) layoutManager;
                        if (layoutManager.getItemCount() > 0) {
                            lastVisiblePosition = staggeredGridLayoutManager.findLastVisibleItemPositions(null)[0];
                        } else {
                            lastVisiblePosition = 0;
                        }
                    }
                    return lastVisiblePosition;
                }
            };


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public BindableRecyclerAdapter(@Nullable List<? extends BindableItem> items) {
        if (items != null) {
            mItems.addAll(items);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getViewType();
    }

    @Override
    public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new BindableViewHolder(DataBindingUtil.inflate(inflater, viewType, parent, false));
    }

    private int canClick = 0;

    public void onBindViewHolder()
    {
        canClick = 1;
    }

    @Override
    public void onBindViewHolder(BindableViewHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        BindableItem item = mItems.get(position);
        if (mCanLoadMore && position == getItemCount() - 1) {
            //bind footer view
            onBindingFooter(binding);
        } else {
            binding.setVariable(BR.item, item);
            if (mItemActionHandler != null) {
                binding.setVariable(BR.actionHandler, mItemActionHandler);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClick(position);
                }
            }
        });
        binding.executePendingBindings();
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onViewRecycled(BindableViewHolder holder) {
        //unbind
        holder.getBinding().unbind();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    BindableItem item = mItems.get(position);
                    return item.getSpanSize(spanCount);
                }
            });
        }
        if (mCanLoadMore) {
            recyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (mCanLoadMore) recyclerView.removeOnScrollListener(onScrollListener);
    }

    //--------------------------------------------------------------------------------

    /*开启自动加载更多*/
    public void enableLoadMore() {
        mCanLoadMore = true;
        mItems.add(getBindingFooter());
        onCheckIsComplete();
    }

    /**
     * 加载成功
     */
    public void onFinishLoad() {
        if (mCanLoadMore) {
            mLoadState = LoadState.NORMAL;
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * 加载失败
     */
    public void onLoadFail() {
        if (mCanLoadMore) {
            mLoadState = LoadState.FAIL;
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * 没有更多数据了
     */
    public void onCompleted() {
        if (mCanLoadMore) {
            mLoadState = LoadState.COMPLETED;
            notifyItemChanged(getItemCount() - 1);
        }
    }

    //重置数据
    public void onReset(@NonNull List<? extends BindableItem> items) {
        mItems.clear();
        mItems.addAll(items);
        if (mCanLoadMore) {
            //重置加载状态
            mLoadState = LoadState.NORMAL;
            //添加脚部
            mItems.add(getBindingFooter());
            onCheckIsComplete();
        }
        notifyDataSetChanged();
    }

    public void add(@NonNull BindableItem item) {
        if (mCanLoadMore) {
            mItems.add(mItems.size() - 1, item);
            notifyItemInserted(getItemCount() - 1);
        } else {
            mItems.add(item);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void add(int startIndex, @NonNull BindableItem item) {
        mItems.add(startIndex, item);
        notifyItemInserted(startIndex);
    }

    //添加数据集合
    public void addAll(@NonNull List<? extends BindableItem> items) {
        if (mCanLoadMore) {
            onCheckIsComplete();
            mItems.addAll(mItems.size() - 1, items);
            notifyItemRangeInserted(getItemCount() - 1, items.size());
        } else {
            this.mItems.addAll(items);
            notifyItemRangeInserted(getItemCount() - 1, items.size());
        }
    }

    /**
     * 添加数据集合
     *
     * @param startPosition where the start position to add
     * @param items         数据集合
     */
    public void addAll(int startPosition, @NonNull List<? extends BindableItem> items) {
        mItems.addAll(startPosition, items);
        notifyItemRangeInserted(startPosition, items.size());
    }




    //用于拖拽
    public void onItemMove(int from, int to) {
        Collections.swap(mItems, from, to);
        notifyItemMoved(from, to);
    }

    //用于滑动删除
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Remove the Item
     * Note: The Item must Override the {@link Object#equals(Object)} method.
     *
     * @param item removed item
     */
    public void remove(@NonNull BindableItem item) {
        int position = mItems.indexOf(item);
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Remove the Item
     *
     * @param position removed item position
     */
    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * clear all items
     */
    public void clear() {
        mItems.clear();
    }

    /**
     * clear all items and notifyDataSetChanged
     */
    public void clearAndNotify() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public BindableItem getItem(int position) {
        return mItems.get(position);
    }

    public List<BindableItem> getItems() {
        return mItems;
    }

    public int getItemSize() {
        return mItems.size();
    }

    public int indexOfItem(@NonNull BindableItem item) {
        return mItems.indexOf(item);
    }

    public void setBindingFooter(@NonNull BindableFooter footer) {
        mBindingFooter = footer;
    }

    public void setItemActionHandler(@NonNull ItemActionHandler itemActionHandler) {
        mItemActionHandler = itemActionHandler;
    }

    protected ItemActionHandler getItemActionHandler() {
        return mItemActionHandler;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        onLoadMoreListener = listener;
    }

    /*bind footer viewHolder*/
    private void onBindingFooter(ViewDataBinding binding) {
        if (mLoadState == LoadState.NORMAL || mLoadState == LoadState.FAIL) {
            binding.getRoot().setOnClickListener(v -> {
                if (onLoadMoreListener != null) {
                    onLoadMore();
                }
            });
        }
        mBindingFooter.onBindLoadState(binding, mLoadState);
    }

    /*检查是否满意更多数据*/
    private void onCheckIsComplete() {
        if (mItems.size() < mPageSize) {
            mLoadState = LoadState.COMPLETED;
        }
    }

    //load more
    private void onLoadMore() {
        //更新加载状态
        mLoadState = LoadState.LOADING;
        //通知回调
        onLoadMoreListener.onLoadMore();
        mHandler.post(() -> {
            //刷新脚部视图
            BindableRecyclerAdapter.this.notifyItemChanged(getItemCount() - 1);
        });
    }

    private BindableItem getBindingFooter() {
        if (mBindingFooter == null) {
            mBindingFooter = BindableFooter.createFooter();
        }
        return mBindingFooter;
    }
}
