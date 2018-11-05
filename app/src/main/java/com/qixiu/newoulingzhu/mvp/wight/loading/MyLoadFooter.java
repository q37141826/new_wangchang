package com.qixiu.newoulingzhu.mvp.wight.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.qixiu.wanchang.R;

public class MyLoadFooter extends LoadingMoreFooter {
    TextView textViewLoadingMore;
    TextView  textViewIsEnd;
    private Context context;
    public MyLoadFooter(Context context) {
        super(context);
        this.context=context;
    }

    public MyLoadFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;

    }

    @Override
    public void initView() {
        super.initView();
        findLoadingMoreTextView();
        textViewIsEnd=new TextView(getContext());
        textViewIsEnd.setText("已经全部加载完毕");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins( (int)getResources().getDimension(R.dimen.textandiconmargin),0,0,0 );
        textViewIsEnd.setLayoutParams(layoutParams);
        textViewIsEnd.setVisibility(GONE);
        addView(textViewIsEnd);
    }

    private void findLoadingMoreTextView() {
        for (int i = 0; i <getChildCount() ; i++) {
            View view=getChildAt(i);
            if(view instanceof TextView){
                TextView currttext= (TextView) view;
                if(currttext.getText().toString().contains("正在加载")){
                    textViewLoadingMore=currttext;
                }
            }
        }
    }

    //是不是加载到底部了
    public void setIsEnd(boolean setIsEnd){
        if(setIsEnd){
            textViewIsEnd.setVisibility(VISIBLE);
            textViewLoadingMore.setVisibility(GONE);
        }else {
            textViewLoadingMore.setVisibility(VISIBLE);
            textViewIsEnd.setVisibility(GONE);
        }
    }
}
