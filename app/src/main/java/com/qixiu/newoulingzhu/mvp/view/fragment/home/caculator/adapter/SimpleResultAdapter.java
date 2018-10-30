package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.ResultBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

/**
 * Created by my on 2018/9/12.
 */

public class SimpleResultAdapter extends RecyclerBaseAdapter {
    private String lableHead = "<p><p  style=\"float:left;\">";
    private String lableMiddle = "</p><p style=\"color:#ff0000;float:left;\">";
    private String lableMiddle02 = "</p><p style=\"float:left;\">";
    private String lableTag = "</p></p>";

    private String contents;
    private String filename;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_simple_result;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new SimpleResultHolder(itemView, context, this);
    }

    public class SimpleResultHolder extends RecyclerBaseHolder {
        TextView textViewTitle;


        public SimpleResultHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            textViewMoney = itemView.findViewById(R.id.textViewMoney);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof ResultBean.OBean) {
                ResultBean.OBean bean = (ResultBean.OBean) mData;
//                textViewMoney.setVisibility(View.GONE);
//                String str = lableHead  + bean.getTitle() +":"+ lableMiddle + bean.getMoney() + lableMiddle02 + "元" + lableTag;
//                textViewTitle.setText(Html.fromHtml(str));
//                textViewMoney.setText(bean.getMoney());
//                textViewTitle.setText(bean.getTitle()+":");
                SpannableString spanStr01 = new SpannableString(bean.getTitle() + ":" + bean.getMoney() + "元");
                spanStr01.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.theme_color)),
                        bean.getTitle().length() + 1, bean.getTitle().length() + 1 + bean.getMoney().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                textViewTitle.setText(spanStr01);
            }
        }
    }
}
