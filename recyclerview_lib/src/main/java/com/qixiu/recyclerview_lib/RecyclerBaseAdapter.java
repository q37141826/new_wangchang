package com.qixiu.recyclerview_lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 高玉恒 on 2017/4/10 0010.
 * 仅供RecyclerView使用的Adapter
 */

public abstract class RecyclerBaseAdapter<D, H extends RecyclerBaseHolder> extends RecyclerView.Adapter<H> implements IAdapter<H>, View.OnClickListener, View.OnLongClickListener {
    private int width_grid;
    List<D> datas = new ArrayList<>();
    private OnRecyclerItemListener<D> mOnItemClickListener;
    private OnRecyclerItemLongListener<D> onRecyclerItemLongListener;
    private ClickListenner<D> clickListenner;//这个接口提供给holder内部回调所用
    private ClickListenner2<D> itemChildClick;//用这个区分item不同的child点击

    public ClickListenner2<D> getItemChildClick() {
        return itemChildClick;
    }
    public void setItemChildClick(ClickListenner2<D> itemChildClick) {
        this.itemChildClick = itemChildClick;
    }



    public ClickListenner<D> getClickListenner() {
        return clickListenner;
    }

    public void setClickListenner(ClickListenner<D> clickListenner) {
        this.clickListenner = clickListenner;
    }



    private Bundle mBundle;
    OnHolderNotifyRefreshListener mOnHolderNotifyRefreshListener;

    public void setOnHolderNotifyRefreshListener(OnHolderNotifyRefreshListener onHolderNotifyRefreshListener) {
        mOnHolderNotifyRefreshListener = onHolderNotifyRefreshListener;
    }

    public interface OnHolderNotifyRefreshListener {
        void onHolderNotifyRefresh(Object data);
    }

    public void holderNotifyRefresh(Object data) {
        if (mOnHolderNotifyRefreshListener != null) {
            mOnHolderNotifyRefreshListener.onHolderNotifyRefresh(data);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onRecyclerItemLongListener != null) {
            onRecyclerItemLongListener.onItemLongClick(v, (D) v.getTag());
        }
        return false;
    }

    protected void setTileWith(View itemView, int rowCount, Context context, int differencevalue) {
        if (width_grid == 0) {
            width_grid = (int) ((AndroidUtils.getDeviceWidth(context)));
        }
        int width = width_grid / rowCount;
        int height = width;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.leftMargin = differencevalue * 2;
        layoutParams.rightMargin = differencevalue * 2;
        layoutParams.topMargin = differencevalue * 4;
        itemView.setLayoutParams(layoutParams);
    }

    @Override

    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (getLayoutId() > 0) {
            //TODO: 发现使用View.inflater布局显示异常，因此推荐使用LayoutInflater
            itemView =
                    LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);

        } else {

            itemView = getLayoutView(parent.getContext());
            if (itemView == null) {
                throw new RuntimeException("您的View都没有，拿什么显示？");
            }
        }
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        H h = createViewHolder(itemView, parent.getContext(), viewType);
        if (getClickListenner() != null) {
            h.setClickListenner(getClickListenner());
        }
        if (getItemChildClick() != null) {
            h.setClickListenner2(getItemChildClick());
        }

        return h;
    }


    public List<D> getDatas() {
        return datas;

    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.setData(datas.get(position));
        holder.bindHolder(position);
    }

    @Override
    public int getItemCount() {

        return datas == null ? 0 : datas.size();
    }


    public View getLayoutView(Context context) {
        return null;
    }

    public final void addDatas(List<D> datas) {
        int insertIndex = this.datas.size();
        if (datas != null && datas.size() > 0) {
            if (this.datas.addAll(datas)) {
                notifyDataSetChanged();
            }

        }

    }


    public void refreshData(List<D> datas) {

        if (this.datas.size() > 0) {
            this.datas.clear();
        }
        this.datas.addAll(datas == null ? new ArrayList<D>() : datas);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemListener<D> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;

    }

    public void setOnLongItemClickListener(OnRecyclerItemLongListener<D> onRecyclerItemLongListener) {
        this.onRecyclerItemLongListener = onRecyclerItemLongListener;

    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,this, (D) v.getTag());
        }
    }

    /**
     * 如果子类要使用Adapter来传递数据请重写该方法
     *
     * @return
     */
    public Bundle getArguments() {

        return mBundle;
    }

    public void setArguments(Bundle arguments) {

        this.mBundle = arguments;
    }

    public interface ClickListenner<T> {
        void click(View view, T t);
    }

    public interface ClickListenner2<T> {
        void itemChildclick(View view, T t, int type, View itemView);
    }
    protected void setTileWith(View itemView, int rowCount, int marginLeft, int marginRight, int marginTop, int marginBottom) {
        if (width_grid == 0) {
            width_grid = (int) ((AndroidUtils.getDeviceWidth(itemView.getContext())));
        }
        int width = width_grid / rowCount - 2*(marginLeft + marginRight);
        int height = width;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.leftMargin = marginLeft;
        layoutParams.rightMargin = marginRight;
        layoutParams.topMargin = marginTop;
        layoutParams.bottomMargin = marginBottom;
        itemView.setLayoutParams(layoutParams);
    }
}
