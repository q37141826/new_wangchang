package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.RaiseResultBean;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.TrafficResultBean;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

public class ResultAdapter extends RecyclerBaseAdapter {

    private String rulsName;
    private String rulesUrl;

    public void setRulesUrl(String rulesUrl) {
        this.rulesUrl = rulesUrl;
    }

    public void setRulsName(String rulsName) {
        this.rulsName = rulsName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_caculation_result;
    }

    @Override
    public Object createViewHolder(View itemView, Context context, int viewType) {
        return new ResultHolder(itemView, context, this);
    }

    public class ResultHolder extends RecyclerBaseHolder {
        private View viewTop05Line, viewBottom05Line, viewBottom5Line;
        private ImageView imageViewSelecte, imageViewRightGo;
        private TextView textViewItemTitle, textViewPrice;
        private RecyclerView recyclerviewFamily;
        private LinearLayout linearlayoutFamily;
        private TextView textViewTotoal, textViewRulesName;
        private LinearLayout linearlayoutInroduce;

        public ResultHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
            super(itemView, context, adapter);
            viewTop05Line = itemView.findViewById(R.id.viewTop05Line);
            viewBottom05Line = itemView.findViewById(R.id.viewBottom05Line);
            viewBottom5Line = itemView.findViewById(R.id.viewBottom5Line);
            imageViewSelecte = itemView.findViewById(R.id.imageViewSelecte);
            imageViewRightGo = itemView.findViewById(R.id.imageViewRightGo);
            textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewTotoal = itemView.findViewById(R.id.textViewTotoal);
            textViewRulesName = itemView.findViewById(R.id.textViewRulesName);
            recyclerviewFamily = itemView.findViewById(R.id.recyclerviewFamily);
            linearlayoutFamily = itemView.findViewById(R.id.linearlayoutFamily);
            linearlayoutInroduce = itemView.findViewById(R.id.linearlayoutInroduce);
        }

        @Override
        public void bindHolder(int position) {
            if (mData instanceof TrafficResultBean.OBean.ListBean) {
                TrafficResultBean.OBean.ListBean bean = (TrafficResultBean.OBean.ListBean) mData;
                //设置根据 左边的选择按钮状态 是否已经加入了总数总计，
                imageViewSelecte.setVisibility(bean.isIsSelected() ? View.VISIBLE : View.INVISIBLE);
                textViewItemTitle.setText(bean.getTitle());
                //根据type来判断是否能点击的右侧箭头
                imageViewRightGo.setVisibility(!bean.getType().equals("fixed") ? View.VISIBLE : View.INVISIBLE);
                //右侧的价钱,在界面  getToaltoMoney统计方法中，将price字段做了处理
                textViewPrice.setText(TextUtils.isEmpty(bean.getPrice()) ? bean.getText() : bean.getPrice());

                /*根据不同的type和在这个type中排第一还是最后，来判断不同厚度的分割线----------------
                * */
                if (bean.getType().equals("fixed") && bean.isLast()) {
                    viewTop05Line.setVisibility(View.VISIBLE);
                    viewBottom5Line.setVisibility(View.VISIBLE);
                    viewBottom05Line.setVisibility(View.GONE);
                } else {
                    viewBottom05Line.setVisibility(View.VISIBLE);
                    viewTop05Line.setVisibility(View.GONE);
                    viewBottom5Line.setVisibility(View.GONE);
                }
                if (bean.getType().equals("fixed")) {
                    viewTop05Line.setVisibility(View.VISIBLE);
                    viewBottom05Line.setVisibility(View.GONE);
                }
                if (bean.getType().equals("url")) {
                    viewBottom5Line.setVisibility(View.VISIBLE);
                }
           /*----------------
                * */
                //如果有展开项目,这个是抚养人的部分，要回传一个子集列表
                if (bean.getoBean() != null) {
                    linearlayoutFamily.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearlayoutFamily.getLayoutParams();
                    params.height = (bean.getoBean().getList().size() + 1) * ArshowContextUtil.dp2px(35);
                    ResultInnerAdapter adapter = new ResultInnerAdapter();
                    recyclerviewFamily.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerviewFamily.setAdapter(adapter);
                    adapter.refreshData(bean.getoBean().getList());
                    textViewTotoal.setText("总计：" + bean.getoBean().getTotal() + "元");
                    bean.setPrice(bean.getoBean().getTotal() + "");
                } else {
                    linearlayoutFamily.setVisibility(View.GONE);
                }
                //----------------------------------

                //如果是最后一个item，那么显示出跳转到另外一个界面的 内容
                if (position == getDatas().size() - 1) {
                    linearlayoutInroduce.setVisibility(View.VISIBLE);
                    textViewRulesName.setText(rulsName);
                    linearlayoutInroduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    textViewRulesName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, GoToActivity.class);
                            intent.putExtra(ConstantString.FILEPATH, rulesUrl);
                            intent.putExtra(ConstantString.TITLE_NAME, rulsName);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    linearlayoutInroduce.setVisibility(View.GONE);
                }
            }
        }
    }

    public class ResultInnerAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_caculator_inner;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new ResultInnerHolder(itemView, context, this);
        }

        public class ResultInnerHolder extends RecyclerBaseHolder {
            TextView textViewCaculatorInner;

            public ResultInnerHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewCaculatorInner = itemView.findViewById(R.id.textViewCaculatorInner);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof RaiseResultBean.OBean.ListBean) {
                    RaiseResultBean.OBean.ListBean bean = (RaiseResultBean.OBean.ListBean) mData;
                    textViewCaculatorInner.setText(bean.getAge() + "岁总抚养费：    " + bean.getMoney());
                }
            }
        }

    }
}
