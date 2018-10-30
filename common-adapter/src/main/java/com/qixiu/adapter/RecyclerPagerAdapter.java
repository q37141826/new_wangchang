package com.qixiu.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Long on 2017/9/28
 */
public abstract class RecyclerPagerAdapter<VH extends RecyclerPagerAdapter.ViewHolder> extends PagerAdapter {

    private final Queue<VH> mCache = new LinkedList<>();
    private final SparseArray<VH> mAttached = new SparseArray<>();

    public abstract VH onCreateViewHolder(@NonNull ViewGroup container);

    public abstract void onBindViewHolder(@NonNull VH holder, int position);

    public void onRecycleViewHolder(@NonNull VH holder) {
    }

    /**
     * Returns ViewHolder for given position if it exists within ViewPager, or null otherwise
     */
    public VH getViewHolder(int position) {
        return mAttached.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        VH holder = mCache.poll();
        if (holder == null) {
            holder = onCreateViewHolder(container);
        }
        mAttached.put(position, holder);

        // We should not use previous layout params, since ViewPager stores
        // important information there which cannot be reused
        container.addView(holder.itemView, null);

        onBindViewHolder(holder, position);
        return holder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        VH holder = (VH) object;
        mAttached.remove(position);
        container.removeView(holder.itemView);
        mCache.offer(holder);
        onRecycleViewHolder(holder);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        ViewHolder holder = (ViewHolder) object;
        return holder.itemView == view;
    }

    @Override
    public int getItemPosition(Object object) {
        // Forcing all views reinitialization when data set changed.
        // It should be safe because we're using views recycling logic.
        return POSITION_NONE;
    }

    public static class ViewHolder {
        public final View itemView;

        public ViewHolder(@NonNull View itemView) {
            this.itemView = itemView;
        }
    }

}
