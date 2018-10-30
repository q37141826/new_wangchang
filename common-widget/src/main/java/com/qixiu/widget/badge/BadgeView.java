package com.qixiu.widget.badge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Long on 2017/11/9
 */
public class BadgeView extends View implements Badge{

    private BadgeViewHelper mBadgeViewHelper;

    public BadgeView(Context context) {
        this(context,null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBadgeViewHelper = new BadgeViewHelper(this, context, attrs, BadgeViewHelper.BadgeGravity.RightCenter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBadgeViewHelper.drawBadge(canvas);
    }

    @Override
    public void showCirclePointBadge() {
        mBadgeViewHelper.showCirclePointBadge();
    }

    @Override
    public void showTextBadge(String badgeText) {
        mBadgeViewHelper.showTextBadge(badgeText);
    }

    @Override
    public void hiddenBadge() {
        mBadgeViewHelper.hiddenBadge();
    }

    @Override
    public void showDrawableBadge(Bitmap bitmap) {
        mBadgeViewHelper.showDrawable(bitmap);
    }

    @Override
    public boolean isShowBadge() {
        return mBadgeViewHelper.isShowBadge();
    }

    @Override
    public BadgeViewHelper getBadgeViewHelper() {
        return mBadgeViewHelper;
    }
}
