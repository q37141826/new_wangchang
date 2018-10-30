package com.qixiu.widget.badge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by Long on 2017/11/9
 */
public interface Badge {

    /**
     * 显示圆点徽章
     */
    void showCirclePointBadge();

    /**
     * 显示文字徽章
     */
    void showTextBadge(String badgeText);

    /**
     * 隐藏徽章
     */
    void hiddenBadge();

    /**
     * 显示图像徽章
     */
    void showDrawableBadge(Bitmap bitmap);

    /**
     * 是否显示徽章
     *
     */
    boolean isShowBadge();

    BadgeViewHelper getBadgeViewHelper();

    int getWidth();

    int getHeight();

    void postInvalidate();

    ViewParent getParent();

    int getId();

    boolean getGlobalVisibleRect(Rect r);

    Context getContext();

    View getRootView();


}
