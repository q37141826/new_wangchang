package com.qixiu.qixiu.utils;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

/**
 * Created by my on 2018/8/9.
 */

public class DrawableUtils {

    public static void setDrawableResouce(TextView textView, Context context, int left, int top, int right, int bottom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } else {
            textView.setCompoundDrawables(context.getResources().getDrawable(left), context.getResources().getDrawable(top),
                    context.getResources().getDrawable(right), context.getResources().getDrawable(bottom));
        }
    }


}
