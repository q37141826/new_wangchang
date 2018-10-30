package com.qixiu.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long on 2017/3/17
 */
public class BottomNavigationView extends LinearLayout {

    private List<BottomNavigationItemView> mButtons = new ArrayList<>();

    // holds the checked id; the selection is empty by default
    private int mCheckedIndex = 0;
    private OnCheckedChangeListener mOnCheckedChangeListener;


    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomNavigationItemView button = (BottomNavigationItemView) v;
            if (!button.isChecked()) {
                int index = mButtons.indexOf(button);
                checked(index);
            }
        }
    };

    public BottomNavigationView(Context context) {
        this(context, null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float density = context.getResources().getDisplayMetrics().density;
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.setElevation(this, 4 * density);
        } else {
            addCompatibilityTopDivider(context);
        }

    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BottomNavigationItemView) {
            final BottomNavigationItemView button = (BottomNavigationItemView) child;
            button.setOnClickListener(onClickListener);
            mButtons.add(button);
        }

        super.addView(child, index, params);
    }


    public void checked(int index) {
        if (index == mCheckedIndex) {
            //ignore same clicked
            return;
        }

        setCheckedState(mCheckedIndex, false);

        setCheckedState(index, true);

        mCheckedIndex = index;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(mCheckedIndex);
        }
    }

    private void setCheckedState(int index, boolean checked) {
        if (index >= 0) {
            BottomNavigationItemView checkedView = mButtons.get(index);
            if (checkedView != null) {
                checkedView.setChecked(checked);
            }
        }
    }

    public int getCheckedIndex() {
        return mCheckedIndex;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public void clearChecked() {
        for (BottomNavigationItemView button : mButtons) {
            button.setChecked(false);
        }
        mCheckedIndex = -1;
    }

    private void addCompatibilityTopDivider(Context context) {
        View divider = new View(context);
        divider.setBackgroundColor(
                ContextCompat.getColor(context, R.color.bottom_navigation_shadow_color));
        FrameLayout.LayoutParams dividerParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(
                        R.dimen.bottom_navigation_shadow_height));
        divider.setLayoutParams(dividerParams);
        addView(divider);
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(int checkedIndex);
    }


}
