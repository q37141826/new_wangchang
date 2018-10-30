package com.qixiu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.qixiu.widget.indicator.CircleIndicator;


/**
 * Created by Long on 2017/10/10
 */
public class BannerView extends FrameLayout {
    private static final String TAG = "BannerView";

    private static final boolean DEBUG = false;

    private static final int DEF_DELAY_TIME = 5000;

    private LoopViewPager   mViewPager;
    private CircleIndicator mCircleIndicator;

    //间隔时间
    private int     mDelayTime = DEF_DELAY_TIME;
    //自动轮播
    private boolean autoLoop   = true;
    //是否正在轮播
    private boolean isLooping;
    //page count
    private int     mCount;

    private final Runnable loopTask = new Runnable() {

        @Override
        public void run() {
            if (isLooping) {
                int page = mViewPager.getCurrentItem() + 1;
                mViewPager.setCurrentItem(page, true);
                postDelayed(loopTask, mDelayTime);
            }
        }
    };

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView);

        //获取轮播图布局资源(替换默认视图)
        int bannerLayoutRes = a.getResourceId(R.styleable.BannerView_banner_layout, 0);
        if (bannerLayoutRes > 0) {
            LayoutInflater.from(context).inflate(bannerLayoutRes, this, true);
        } else {
            LayoutInflater.from(context).inflate(R.layout.abc_default_banner_view, this, true);
        }

        //获取轮播间隔时间
        mDelayTime = a.getInteger(R.styleable.BannerView_banner_delay, DEF_DELAY_TIME);
        autoLoop = a.getBoolean(R.styleable.BannerView_banner_auto_loop, true);

        a.recycle();

        mViewPager = findViewById(R.id.pager);
        mCircleIndicator = findViewById(R.id.circle_indicator);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean noAdapter = mViewPager.getAdapter() == null;
        if (DEBUG) {
            Log.d(TAG, "onAttachedToWindow: ---- " + noAdapter);
        }
        if (autoLoop && !noAdapter && mCount > 1) onStartLoop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        boolean noAdapter = mViewPager.getAdapter() == null;
        if (DEBUG) {
            Log.d(TAG, "onDetachedFromWindow: ---- " + noAdapter);
        }
        if (autoLoop && !noAdapter && mCount > 1) onStopLoop();
    }

    //触碰控件的时候，翻页应该停止，离开的时候重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ||
                action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (autoLoop)
                onStartLoop();
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (autoLoop)
                onStopLoop();
        }
        return super.dispatchTouchEvent(ev);
    }


    //设置PagerAdapter
    public void setAdapter(@NonNull PagerAdapter adapter) {
        mCount = adapter.getCount();

        mViewPager.setAdapter(adapter);
        //初始化圆点指示器
        mCircleIndicator.setViewPager(mViewPager);
        if (mCount == 1) {
            mCircleIndicator.setVisibility(GONE);
            onStopLoop();
            mViewPager.setNoScroll(true);
        }
    }

    public void setCircleIndicatorGone() {
        mCircleIndicator.setVisibility(GONE);
    }

    //设置翻页效果
    public void setPageTransformer(ViewPager.PageTransformer pageTransformer) {
        mViewPager.setPageTransformer(true, pageTransformer);
    }

    //获取ViewPager
    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void onStartLoop() {
        if (mCount > 1) {
            startLoop();
        }
    }

    public void onStartLoop(int delayTime) {
        if (mCount > 1) {
            this.mDelayTime = delayTime;
            startLoop();
        }
    }

    public void onStopLoop() {
        if (mCount > 1) {
            isLooping = false;
            removeCallbacks(loopTask);
        }
    }

    private void startLoop() {
        autoLoop = true;
        if (isLooping) {
            onStopLoop();
        }
        isLooping = true;
        postDelayed(loopTask, mDelayTime);
    }

}
