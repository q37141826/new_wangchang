package com.qixiu.widget.badge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.qixiu.widget.R;


/**
 * Created by Long on 2017/11/9
 */
@SuppressWarnings("WeakerAccess")
public class BadgeViewHelper {

    private Bitmap mBitmap;
    private Badge mBadge;
    private Paint mBadgePaint;

    /**
     * 徽章背景色
     */
    private int mBadgeBgColor;
    /**
     * 徽章文本的颜色
     */
    private int mBadgeTextColor;
    /**
     * 徽章文本字体大小
     */
    private int mBadgeTextSize;
    /**
     * 徽章背景与宿主控件上下边缘间距离
     */
    private int mBadgeVerticalMargin;
    /**
     * 徽章背景与宿主控件左右边缘间距离
     */
    private int mBadgeHorizontalMargin;
    /***
     * 徽章文本边缘与徽章背景边缘间的距离
     */
    private int mBadgePadding;
    /**
     * 徽章文本
     */
    private String mBadgeText;
    /**
     * 徽章文本所占区域大小
     */
    private Rect mBadgeNumberRect;
    /**
     * 是否显示Badge
     */
    private boolean mIsShowBadge;
    /**
     * 徽章在宿主控件中的位置
     */
    private BadgeGravity mBadgeGravity;
    /**
     * 整个徽章所占区域
     */
    private RectF mBadgeRectF;


    /***
     * 徽章描边宽度
     */
    private int mBadgeBorderWidth;
    /***
     * 徽章描边颜色
     */
    private int mBadgeBorderColor;

    private boolean mIsShowDrawable = false;

    public BadgeViewHelper(Badge badge, Context context, AttributeSet attrs, BadgeGravity gravity) {
        mBadge = badge;
        initDefaultAttrs(context, gravity);
        initCustomAttrs(context, attrs);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs(Context context, BadgeGravity gravity) {
        mBadgeNumberRect = new Rect();
        mBadgeRectF = new RectF();
        mBadgeBgColor = Color.RED;
        mBadgeTextColor = Color.WHITE;
        mBadgeTextSize = BadgeUtils.sp2px(context, 10);
        mBadgePaint = new Paint();
        mBadgePaint.setAntiAlias(true);
        mBadgePaint.setStyle(Paint.Style.FILL);
        // 设置mBadgeText居中，保证mBadgeText长度为1时，文本也能居中
        mBadgePaint.setTextAlign(Paint.Align.CENTER);
        mBadgePadding = BadgeUtils.dp2px(context, 4);
        mBadgeVerticalMargin = BadgeUtils.dp2px(context, 4);
        mBadgeHorizontalMargin = BadgeUtils.dp2px(context, 4);
        mBadgeGravity = gravity;
        mIsShowBadge = false;
        mBadgeText = null;
        mBitmap = null;
        mBadgeBorderColor = Color.WHITE;

    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BadgeView_badge_bgColor) {
            mBadgeBgColor = typedArray.getColor(attr, mBadgeBgColor);
        } else if (attr == R.styleable.BadgeView_badge_textColor) {
            mBadgeTextColor = typedArray.getColor(attr, mBadgeTextColor);
        } else if (attr == R.styleable.BadgeView_badge_textSize) {
            mBadgeTextSize = typedArray.getDimensionPixelSize(attr, mBadgeTextSize);
        } else if (attr == R.styleable.BadgeView_badge_verticalMargin) {
            mBadgeVerticalMargin = typedArray.getDimensionPixelSize(attr, mBadgeVerticalMargin);
        } else if (attr == R.styleable.BadgeView_badge_horizontalMargin) {
            mBadgeHorizontalMargin = typedArray.getDimensionPixelSize(attr, mBadgeHorizontalMargin);
        } else if (attr == R.styleable.BadgeView_badge_padding) {
            mBadgePadding = typedArray.getDimensionPixelSize(attr, mBadgePadding);
        } else if (attr == R.styleable.BadgeView_badge_gravity) {
            int ordinal = typedArray.getInt(attr, mBadgeGravity.ordinal());
            mBadgeGravity = BadgeGravity.values()[ordinal];
        } else if (attr == R.styleable.BadgeView_badge_borderWidth) {
            mBadgeBorderWidth = typedArray.getDimensionPixelSize(attr, mBadgeBorderWidth);
        } else if (attr == R.styleable.BadgeView_badge_borderColor) {
            mBadgeBorderColor = typedArray.getColor(attr, mBadgeBorderColor);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        mBadgePaint.setTextSize(mBadgeTextSize);
    }

    public void setBadgeBackgroundColor(@ColorInt int backgroundColor) {
        mBadgeBgColor = backgroundColor;
        mBadge.postInvalidate();
    }

    public void setBadgeTextColor(@ColorInt int badgeTextColor) {
        mBadgeTextColor = badgeTextColor;
        mBadge.postInvalidate();
    }

    //设置微章文字大小，PX
    public void setBadgeTextSize(@Px int badgeTextSize) {
        if (badgeTextSize >= 0) {
            mBadgeTextSize = badgeTextSize;
            mBadgePaint.setTextSize(mBadgeTextSize);
            mBadge.postInvalidate();
        }
    }

    public void setBadgeVerticalMargin(@Px int verticalMargin) {
        if (verticalMargin >= 0) {
            mBadgeVerticalMargin = verticalMargin;
            mBadge.postInvalidate();
        }
    }

    public void setBadgeHorizontalMargin(@Px int horizontalMargin) {
        if (horizontalMargin >= 0) {
            mBadgeHorizontalMargin = horizontalMargin;
            mBadge.postInvalidate();
        }
    }

    public void setBadgePadding(@Px int padding) {
        if (padding >= 0) {
            mBadgePadding = padding;
            mBadge.postInvalidate();
        }
    }

    public void setBadgeGravity(BadgeGravity badgeGravity) {
        if (mBadgeGravity != badgeGravity) {
            mBadgeGravity = badgeGravity;
            mBadge.postInvalidate();
        }
    }

    public void setBadgeBorderWidth(@Px int borderWidth) {
        if (borderWidth >= 0) {
            mBadgeBorderWidth = borderWidth;
            mBadge.postInvalidate();
        }
    }

    public void setBadgeBorderColor(@ColorInt int borderColor) {
        if (mBadgeBorderColor != borderColor) {
            mBadgeBorderColor = borderColor;
            mBadge.postInvalidate();
        }
    }

    public void drawBadge(Canvas canvas) {
        if (mIsShowBadge) {
            if (mIsShowDrawable) {
                drawDrawableBadge(canvas);
            } else {
                drawTextBadge(canvas);
            }
        }
    }

    /**
     * 绘制图像徽章
     *
     */
    private void drawDrawableBadge(Canvas canvas) {
        mBadgeRectF.left = mBadge.getWidth() - mBadgeHorizontalMargin - mBitmap.getWidth();
        mBadgeRectF.top = mBadgeVerticalMargin;
        switch (mBadgeGravity) {
            case RightTop:
                mBadgeRectF.top = mBadgeVerticalMargin;
                break;
            case RightCenter:
                mBadgeRectF.top = (mBadge.getHeight() - mBitmap.getHeight()) / 2;
                break;
            case RightBottom:
                mBadgeRectF.top = mBadge.getHeight() - mBitmap.getHeight() - mBadgeVerticalMargin;
                break;
            default:
                break;
        }
        canvas.drawBitmap(mBitmap, mBadgeRectF.left, mBadgeRectF.top, mBadgePaint);
        mBadgeRectF.right = mBadgeRectF.left + mBitmap.getWidth();
        mBadgeRectF.bottom = mBadgeRectF.top + mBitmap.getHeight();
    }

    /**
     * 绘制文字徽章
     *
     */
    private void drawTextBadge(Canvas canvas) {
        String badgeText = "";
        if (!TextUtils.isEmpty(mBadgeText)) {
            badgeText = mBadgeText;
        }
        // 获取文本宽所占宽高
        mBadgePaint.getTextBounds(badgeText, 0, badgeText.length(), mBadgeNumberRect);
        // 计算徽章背景的宽高
        int badgeHeight = mBadgeNumberRect.height() + mBadgePadding * 2;
        int badgeWidth;
        // 当mBadgeText的长度为1或0时，计算出来的高度会比宽度大，此时设置宽度等于高度
        if (badgeText.length() == 1 || badgeText.length() == 0) {
            badgeWidth = badgeHeight;
        } else {
            badgeWidth = mBadgeNumberRect.width() + mBadgePadding * 2;
        }

        // 计算徽章背景上下的值
        mBadgeRectF.top = mBadgeVerticalMargin;
        mBadgeRectF.bottom = mBadge.getHeight() - mBadgeVerticalMargin;
        switch (mBadgeGravity) {
            case RightTop:
                mBadgeRectF.bottom = mBadgeRectF.top + badgeHeight;
                break;
            case RightCenter:
                mBadgeRectF.top = (mBadge.getHeight() - badgeHeight) / 2;
                mBadgeRectF.bottom = mBadgeRectF.top + badgeHeight;
                break;
            case RightBottom:
                mBadgeRectF.top = mBadgeRectF.bottom - badgeHeight;
                break;
            default:
                break;
        }

        // 计算徽章背景左右的值
        mBadgeRectF.right = mBadge.getWidth() - mBadgeHorizontalMargin;
        mBadgeRectF.left = mBadgeRectF.right - badgeWidth;

        if (mBadgeBorderWidth > 0) {
            // 设置徽章边框景色
            mBadgePaint.setColor(mBadgeBorderColor);
            // 绘制徽章边框背景
            canvas.drawRoundRect(mBadgeRectF, badgeHeight / 2, badgeHeight / 2, mBadgePaint);

            // 设置徽章背景色
            mBadgePaint.setColor(mBadgeBgColor);
            // 绘制徽章背景
            canvas.drawRoundRect(new RectF(mBadgeRectF.left + mBadgeBorderWidth,
                    mBadgeRectF.top + mBadgeBorderWidth,
                    mBadgeRectF.right - mBadgeBorderWidth,
                    mBadgeRectF.bottom - mBadgeBorderWidth),
                    (badgeHeight - 2 * mBadgeBorderWidth) / 2,
                    (badgeHeight - 2 * mBadgeBorderWidth) / 2, mBadgePaint);

        } else {
            // 设置徽章背景色
            mBadgePaint.setColor(mBadgeBgColor);
            // 绘制徽章背景
            canvas.drawRoundRect(mBadgeRectF, badgeHeight / 2, badgeHeight / 2, mBadgePaint);
        }

        if (!TextUtils.isEmpty(mBadgeText)) {
            // 设置徽章文本颜色
            mBadgePaint.setColor(mBadgeTextColor);
            // initDefaultAttrs方法中设置了mBadgeText居中，此处的x为徽章背景的中心点y
            float x = mBadgeRectF.left + badgeWidth / 2;
            // 注意：绘制文本时的y是指文本底部，而不是文本的中间
            float y = mBadgeRectF.bottom - mBadgePadding;
            // 绘制徽章文本
            canvas.drawText(badgeText, x, y, mBadgePaint);
        }
    }

    public void showCirclePointBadge() {
        showTextBadge("");
    }

    public void showTextBadge(@Nullable String text) {
        if (text != null) {
            mIsShowDrawable = false;
            mBadgeText = text;
            mIsShowBadge = true;
            mBadge.postInvalidate();
        }
    }

    public void hiddenBadge() {
        mIsShowBadge = false;
        mBadge.postInvalidate();
    }

    public boolean isShowBadge() {
        return mIsShowBadge;
    }

    public void showDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mIsShowDrawable = true;
        mIsShowBadge = true;
        mBadge.postInvalidate();
    }

    public boolean isShowDrawable() {
        return mIsShowDrawable;
    }

    public RectF getBadgeRectF() {
        return mBadgeRectF;
    }

    public int getBadgePadding() {
        return mBadgePadding;
    }

    public String getBadgeText() {
        return mBadgeText;
    }

    public int getBadgeBgColor() {
        return mBadgeBgColor;
    }

    public int getBadgeTextColor() {
        return mBadgeTextColor;
    }

    public int getBadgeTextSize() {
        return mBadgeTextSize;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }


    public View getRootView() {
        return mBadge.getRootView();
    }

    public enum BadgeGravity {
        RightTop,
        RightCenter,
        RightBottom
    }

}
