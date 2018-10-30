package com.qixiu.qixiu.titleview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.qixiu.qixiu.R;


public class TitleView extends BaseView {
    TextView title_text, right_text;
    TextView backImageView;

    public TitleView(Context context) {
        super(context);
    }

    public View getLeftView() {
        return backImageView;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.title_layout;
    }

    @Override
    protected void initView() {
        title_text = (TextView) mView.findViewById(R.id.title_text);

        right_text = (TextView) mView.findViewById(R.id.right_text);
        backImageView = (TextView) mView.findViewById(R.id.back_image);
    }

    public void setTitle(String name) {
        title_text.setText(name);
    }

    public void setTitle(int resId) {
        title_text.setText(resId);
    }

    public void setBackListener(View.OnClickListener pClickListener) {
        backImageView.setOnClickListener(pClickListener);
    }

    public void setRightListener(View.OnClickListener pClickListener) {
        right_text.setOnClickListener(pClickListener);
    }

    public void setRightEnable( boolean enable) {
        right_text.setEnabled(enable);
    }

    public void setBackVisibility(int v) {
        backImageView.setVisibility(v);
    }

    public void setRightTextVisible(int v) {
        right_text.setVisibility(v);
    }

    public void setLeftText(String text) {
        backImageView.setText(text);
    }

    public void setRightText(String text) {
        right_text.setVisibility(View.VISIBLE);
        right_text.setText(text);
    }

    public void setRightImage(Context context,int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            right_text.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        } else {
            right_text.setCompoundDrawables(context.getResources().getDrawable(resource), null, null, null);
        }
    }

    public void setLeftImage(Context context,int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            backImageView.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
        } else {
            backImageView.setCompoundDrawables(context.getResources().getDrawable(resource), null, null, null);
        }

    }

    public void setRightValue(String text, View.OnClickListener pClickListener) {
        right_text.setText(text);
        right_text.setOnClickListener(pClickListener);
    }

    public int getLeftId() {
        return backImageView.getId();
    }

    public int getRightId() {
        return right_text.getId();
    }
    public TextView getRightText(){
        return right_text;
    }

    public void setTitle_textColor(int color) {
        title_text.setTextColor(color);
    }
}
