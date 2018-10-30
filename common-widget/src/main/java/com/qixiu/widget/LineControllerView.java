package com.qixiu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Long on 2017/2/9
 */
public class LineControllerView extends FrameLayout {

    /**
     * 默认左边主要文字大小(sp)
     */
    private static final int DEF_TEXT_PRIMARY_SIZE = 16;

    /**
     * 默认右边次要文字大小(sp)
     */
    private static final int DEF_TEXT_SECONDARY_SIZE = 12;

    private static final int DEF_TEXT_PRIMARY_COLOR = 0xff212121;
    private static final int DEF_TEXT_SECONDARY_COLOR = 0xff757575;


    /*左边Icon*/
    private ImageView mIconView;
    /*右边箭头*/
    private ImageView mArrowView;

    /*主要文字*/
    private TextView mPrimaryTextView;
    /*右边次要文字*/
    private TextView mSecondaryTextView;

    /*开关*/
    private Switch mSwitch;


    public LineControllerView(Context context) {
        this(context, null);
    }

    public LineControllerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int scaledDensity = (int) context.getResources().getDisplayMetrics().scaledDensity;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineControllerView,
                defStyleAttr, 0);

        Drawable leftIcon = a.getDrawable(R.styleable.LineControllerView_leftIcon);
        Drawable rightArrowIcon = a.getDrawable(R.styleable.LineControllerView_rightArrowIcon);

        String textPrimary = a.getString(R.styleable.LineControllerView_textPrimary);
        String textSecondary = a.getString(R.styleable.LineControllerView_textSecondary);

        final float textPrimarySize = a.getDimension(R.styleable.LineControllerView_textPrimarySize,
                scaledDensity * DEF_TEXT_PRIMARY_SIZE);

        final float textSecondarySize = a.getDimension(R.styleable.LineControllerView_textSecondarySize,
                scaledDensity * DEF_TEXT_SECONDARY_SIZE);

        final int textPrimaryColor = a.getColor(R.styleable.LineControllerView_textPrimaryColor,
                DEF_TEXT_PRIMARY_COLOR);

        final int textSecondaryColor = a.getColor(R.styleable.LineControllerView_textSecondaryColor,
                DEF_TEXT_SECONDARY_COLOR);


        //是否显示右边箭头图标
        boolean showRightArrowIcon = a.getBoolean(R.styleable.LineControllerView_showRightArrow, false);
        //是否显示开关按钮
        boolean showSwitch = a.getBoolean(R.styleable.LineControllerView_showSwitch, false);
        //开关按钮是否打开
        boolean switchChecked = a.getBoolean(R.styleable.LineControllerView_switchChecked, false);

        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.abc_line_controller_view, this);

        initViews();

        //设置左边图标
        if (leftIcon != null) {
            mIconView.setImageDrawable(leftIcon);
        } else {
            mIconView.setVisibility(GONE);
        }

        //设置主要文字
        mPrimaryTextView.setTextColor(textPrimaryColor);
        mPrimaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textPrimarySize);
        mPrimaryTextView.setText(textPrimary);
        if (mIconView.getVisibility() == GONE) {
            //没有设置icon,清除margin
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPrimaryTextView.getLayoutParams();
            params.setMargins(0, 0, params.rightMargin, 0);
            mPrimaryTextView.setLayoutParams(params);
        }

        //是否显示开关按钮
        if (showSwitch) {
            mSwitch.setVisibility(VISIBLE);
            mSwitch.setChecked(switchChecked);
            //开关按钮显示时，隐藏右边箭头，文字
            mArrowView.setVisibility(GONE);
            mSecondaryTextView.setVisibility(GONE);
            return;
        }

        //设置右边箭头图标
        if (showRightArrowIcon && rightArrowIcon != null) {
            mArrowView.setImageDrawable(rightArrowIcon);
        } else {
            mArrowView.setVisibility(GONE);
        }


        //设置右边文字
        if (!TextUtils.isEmpty(textSecondary)) {
            mSecondaryTextView.setTextColor(textSecondaryColor);
            mSecondaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSecondarySize);
            mSecondaryTextView.setText(textSecondary);
            if (mArrowView.getVisibility() != VISIBLE) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                        mSecondaryTextView.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
        } else {
            mSecondaryTextView.setVisibility(GONE);
        }

    }

    private void initViews() {
        mIconView = (ImageView) findViewById(R.id.icon);
        mArrowView = (ImageView) findViewById(R.id.right_arrow);

        mPrimaryTextView = (TextView) findViewById(R.id.text_primary);
        mSecondaryTextView = (TextView) findViewById(R.id.text_secondary);

        mSwitch = (Switch) findViewById(R.id.switch_);

    }


    public TextView getPrimaryTextView() {
        return mPrimaryTextView;
    }

    public void setPrimaryText(String text) {
        mPrimaryTextView.setText(text);
    }

    public TextView getSecondaryTextView() {
        return mSecondaryTextView;
    }

    public void setSecondaryText(String text) {
        if (mSecondaryTextView.getVisibility() != VISIBLE) {
            mSecondaryTextView.setVisibility(VISIBLE);
        }
        mSecondaryTextView.setText(text);
    }

    public CharSequence getSecondaryText() {
        return mSecondaryTextView.getText();
    }

    public void setLeftIcon(int res) {
        mIconView.setImageResource(res);
    }

    public void setChecked(boolean checked) {
        if (mSwitch.getVisibility() == VISIBLE) {
            mSwitch.setChecked(checked);
        }
    }

    public void toggle() {
        if (mSwitch.getVisibility() == VISIBLE) {
            mSwitch.toggle();
        }
    }

    public boolean isChecked() {
        return mSwitch.getVisibility() == VISIBLE && mSwitch.isChecked();
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        if (mSwitch.getVisibility() == VISIBLE) {
            mSwitch.setOnCheckedChangeListener(listener);
        }
    }

}
