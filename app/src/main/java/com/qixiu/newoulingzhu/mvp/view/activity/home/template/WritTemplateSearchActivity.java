package com.qixiu.newoulingzhu.mvp.view.activity.home.template;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.template.WritTemplateFragment;
import com.qixiu.wanchang.R;


/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class WritTemplateSearchActivity extends TitleActivity implements View.OnTouchListener {

    private EditText mEditText;
    private WritTemplateFragment mWritTemplateFragment;
    private WritTemplateFragment.TemplateEditTextListener mTemplateEditTextListener;

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("文书模板");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditText = (EditText) findViewById(R.id.et_search);
        mEditText.setOnTouchListener(this);
        mWritTemplateFragment = new WritTemplateFragment();
        addFragment(R.id.fl_templatelist, mWritTemplateFragment, WritTemplateFragment.class.getSimpleName());
        mTemplateEditTextListener = new WritTemplateFragment.TemplateEditTextListener(mWritTemplateFragment);
        mEditText.addTextChangedListener(mTemplateEditTextListener);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_writ_template;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Drawable drawable = mEditText.getCompoundDrawables()[2];
            int width = drawable.getBounds().width();
            if (event.getX() + 4 * width >= (mEditText.getRight() - 2 * width)) {
                mWritTemplateFragment.onSearch(mEditText.getText().toString().trim());
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        if (mEditText != null) {
            mEditText.removeTextChangedListener(mTemplateEditTextListener);
        }
        super.onDestroy();


    }
}
