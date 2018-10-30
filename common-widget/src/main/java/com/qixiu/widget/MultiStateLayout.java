package com.qixiu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Long on 2016/2/24
 * 根据状态显示加载中，加载错误，空数据界面
 */
public class MultiStateLayout extends FrameLayout {

    public static final int VIEW_STATE_CONTENT = 0;
    public static final int VIEW_STATE_LOADING = 1;
    public static final int VIEW_STATE_ERROR = 2;
    public static final int VIEW_STATE_EMPTY = 3;

    private LayoutInflater mInflater;

    private View mContentView;                  //内容视图
    private View mLoadingView;                  //加载状态视图
    private View mErrorView;                    //加载错误视图
    private View mEmptyView;                    //空数据视图

    //重试按钮点击监听器
    private View.OnClickListener mReloadClickListener;

    private int mViewState = VIEW_STATE_LOADING;     //默认状态，加载中

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_STATE_CONTENT, VIEW_STATE_LOADING, VIEW_STATE_ERROR, VIEW_STATE_EMPTY})
    public @interface ViewState {
    }


    public MultiStateLayout(Context context) {
        this(context, null);
    }

    public MultiStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStateLayout, 0, 0);
        //获取加载视图引用
        int loadingResId = typedArray.getResourceId(R.styleable.MultiStateLayout_loading_view, -1);
        if (loadingResId > -1) {
            mLoadingView = mInflater.inflate(loadingResId, this, false);
            addView(mLoadingView, mLoadingView.getLayoutParams());
        }
        //获取错误视图引用
        int errorResId = typedArray.getResourceId(R.styleable.MultiStateLayout_error_view, -1);
        if (errorResId > -1) {
            mErrorView = mInflater.inflate(errorResId, this, false);
            addView(mErrorView, mErrorView.getLayoutParams());
            //handle reload btn click
            View reload = mErrorView.findViewById(R.id.reload);
            if (reload != null) {
                reload.setOnClickListener(v -> {
                    if (mReloadClickListener != null) {
                        mReloadClickListener.onClick(v);
                    }
                });
            }
        }
        //获取空视图引用
        int emptyResId = typedArray.getResourceId(R.styleable.MultiStateLayout_empty_view, -1);
        if (emptyResId > -1) {
            mEmptyView = mInflater.inflate(emptyResId, this, false);
            addView(mEmptyView, mEmptyView.getLayoutParams());
        }
        //获取默认状态
        mViewState = typedArray.getInt(R.styleable.MultiStateLayout_view_state, VIEW_STATE_LOADING);

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) {
            throw new IllegalArgumentException("Content view is not defined");
        }
        setView();
    }


    @Override
    public void addView(View child) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Nullable
    public View getView(@ViewState int viewState) {
        switch (viewState) {
            case VIEW_STATE_LOADING:
                return mLoadingView;
            case VIEW_STATE_ERROR:
                return mErrorView;
            case VIEW_STATE_EMPTY:
                return mEmptyView;
            case VIEW_STATE_CONTENT:
                return mContentView;
            default:
                return null;
        }
    }

    @ViewState
    public int getViewState() {
        return mViewState;
    }

    /**
     * 设置VIEW显示状态,刷新界面
     *
     * @param state state
     */
    public void setViewState(@ViewState int state) {
        if (mViewState != state) {
            mViewState = state;
            setView();
        }
    }

    /**
     * 设置重试按钮点击事件监听
     *
     * @param listener 点击事件监听器
     */
    public void setOnReloadClickListener(View.OnClickListener listener) {
        mReloadClickListener = listener;
    }

    /**
     * 判断是否是 mContentView
     *
     * @param view v
     * @return boolean
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mErrorView && view != mEmptyView;
    }

    /**
     * 根据不同状态显示不同界面
     */
    private void setView() {
        switch (mViewState) {
            case VIEW_STATE_LOADING:
                if (mLoadingView == null) {
                    throw new NullPointerException("Loading view is a nullPoint");
                }
                mLoadingView.setVisibility(View.VISIBLE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;
            case VIEW_STATE_ERROR:
                if (mErrorView == null) {
                    throw new NullPointerException("Error view is a NullPoint");
                }
                mErrorView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;
            case VIEW_STATE_EMPTY:
                if (mEmptyView == null) {
                    throw new NullPointerException("Empty view is a NullPoint");
                }
                mEmptyView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                break;
            case VIEW_STATE_CONTENT:
            default:
                if (mContentView == null) {
                    throw new NullPointerException("Content view is a NullPoint");
                }
                mContentView.setVisibility(View.VISIBLE);
                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 根据状态,在代码中动态替换 mLoadingView,mErrorView,mEmptyView,
     *
     * @param view          view
     * @param state         state
     * @param switchToState 是否切换状态
     */
    public void setViewForState(View view, @ViewState int state, boolean switchToState) {
        switch (state) {
            case VIEW_STATE_LOADING:
                if (mLoadingView != null) {
                    removeView(mLoadingView);
                }
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case VIEW_STATE_ERROR:
                if (mErrorView != null) {
                    removeView(mErrorView);
                }
                mErrorView = view;
                addView(mErrorView);
                break;
            case VIEW_STATE_EMPTY:
                if (mEmptyView != null) {
                    removeView(mEmptyView);
                }
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case VIEW_STATE_CONTENT:
                if (mContentView != null) {
                    removeView(mContentView);
                }
                mContentView = view;
                addView(mContentView);
                break;
        }
        if (switchToState) {
            setViewState(state);
        }
    }

    public void setViewForState(View view, @ViewState int state) {
        setViewForState(view, state, false);
    }

    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state, boolean switchToState) {
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    public void setViewForState(@LayoutRes int layoutRes, @ViewState int state) {
        setViewForState(layoutRes, state, false);
    }

}
