package com.qixiu.newoulingzhu.mvp.view.activity.home.template;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.qixiu.newoulingzhu.beans.home.search.SearchHotBean;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.homne.template.WritTemplateTagAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.template.WritTemplateFragment;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class WritTemplateActivity extends TitleActivity implements OnRecyclerItemListener, View.OnTouchListener {


    private RecyclerView mRv_template_tag;
    private WritTemplateTagAdapter mWritTemplateTagAdapter;
    private List<SearchHotBean.SearchHotInfo> mSearchHotInfos;
    private List<SearchHotBean.SearchHotInfo> mSearchHotInfos1;
    private boolean isAllunfold;
    private EditText mEditText;

    public static void start(Context context){
        context.startActivity(new Intent (context,WritTemplateActivity.class));
    }


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
        mEditText.setFocusable(false);
        mEditText.setOnTouchListener(this);
        addFragment(R.id.fl_templatelist, new WritTemplateFragment(), WritTemplateFragment.class.getSimpleName());
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
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        int position = mRv_template_tag.getChildLayoutPosition(v);
        if (position == mWritTemplateTagAdapter.getDatas().size()) {
            if (isAllunfold) {
                mWritTemplateTagAdapter.refreshData(mSearchHotInfos1);
            } else {
                mWritTemplateTagAdapter.refreshData(mSearchHotInfos);
            }
            isAllunfold = !isAllunfold;
        } else {

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startActivity(new Intent(this, WritTemplateSearchActivity.class));
            return true;
        }
        return false;
    }
}
