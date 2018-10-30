package com.qixiu.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.widget.badge.MaterialBadgeTextView;


/**
 * Created by Long on 2017/3/17
 */
public class BottomNavigationItemView extends RelativeLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private boolean mChecked = false;

    private ImageView mIcon;       //图标
    private TextView mLabel;       //label
    private MaterialBadgeTextView mBadgeView;
    private boolean isShowBadge = false;

    public BottomNavigationItemView(Context context) {
        this(context, null);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float density = context.getResources().getDisplayMetrics().density;
        setPadding(0, (int) (8 * density), 0, (int) (8 * density));

        // get attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationItemView);
        Drawable iconDrawable = a.getDrawable(R.styleable.BottomNavigationItemView_icon);
        String label = a.getString(R.styleable.BottomNavigationItemView_label);
        ColorStateList labelColor = a.getColorStateList(R.styleable.BottomNavigationItemView_labelColor);
        mChecked = a.getBoolean(R.styleable.BottomNavigationItemView_android_checked, false);
        a.recycle();

        //inflate layout

        LayoutInflater.from(context).inflate(R.layout.bottom_navigation_item, this, true);

        mIcon = findViewById(R.id.icon);
        mLabel = findViewById(R.id.label);
        mBadgeView = findViewById(R.id.badge);
        //默认不显示
        mBadgeView.setBadgeCount(0, true);

        mIcon.setImageDrawable(iconDrawable);
        if (labelColor != null) {
            mLabel.setTextColor(labelColor);
        }
        mLabel.setText(label);

        setChecked(mChecked);

    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void toggleBadge() {
        if (isShowBadge) {
            hideBadge();
        } else {
            showBadge();
        }
    }

    //显示数字样式
    public void setBadgeCount(int count) {
        mBadgeView.setBadgeCount(count);
    }

    //显示红点样式
    public void showRedPoint() {
        mBadgeView.setHighLightMode();
    }

    public void showBadge() {
        isShowBadge = true;
        mBadgeView.setVisibility(VISIBLE);
    }

    public void hideBadge() {
        isShowBadge = false;
        mBadgeView.setVisibility(GONE);
    }

}
