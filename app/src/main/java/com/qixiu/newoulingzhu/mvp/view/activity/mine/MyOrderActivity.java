package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.interf.IPay;
import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.mine_bean.order.MyConsultingBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.myorderdetails.OrderDetailsActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class MyOrderActivity extends RequstActivity implements XRecyclerView.LoadingListener, CompoundButton.OnCheckedChangeListener, IPay, OnRecyclerItemListener {
    @BindView(R.id.imageViewNothing)
    ImageView imageViewNothing;
    private int pageNo = 1, pageSize = 10;
    private String type = "1";//	订单类型：1为 处理中，2 为 已完成；
    @BindView(R.id.radioOnTheWay)
    RadioButton radioOnTheWay;
    @BindView(R.id.radioFinished)
    RadioButton radioFinished;
    @BindView(R.id.recyclerViewOder)
    XRecyclerView recyclerViewOder;
//    @BindView(R.id.swipOrder)
//    SwipeRefreshLayout swipOrder;
    OrderAdapter orderAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyOrderActivity.class));
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("我的订单");
        requsteData();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void onInitViewNew() {
        XrecyclerViewUtil.setXrecyclerView(recyclerViewOder, this, this, true, 1, null);
        radioOnTheWay.setOnCheckedChangeListener(this);
        radioFinished.setOnCheckedChangeListener(this);
        orderAdapter = new OrderAdapter();
        recyclerViewOder.setAdapter(orderAdapter);
        RefreshHeader refreshHeader=new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerViewOder.setRefreshHeader(refreshHeader);
//        swipOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageNo = 1;
//                requsteData();
//            }
//        });
//        swipOrder.setColorSchemeResources(R.color.theme_color);
        orderAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void requsteData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        map.put("type", type + "");
        MyConsultingBean bean = new MyConsultingBean();
        post(ConstantUrl.myConsiltingUrl, map, bean);
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof MyConsultingBean) {
            MyConsultingBean bean = (MyConsultingBean) data;
            if (pageNo == 1) {
                orderAdapter.refreshData(bean.getO().getData());
            } else {
                orderAdapter.addDatas(bean.getO().getData());
            }
        }
        recyclerViewOder.loadMoreComplete();
        recyclerViewOder.refreshComplete();
//        swipOrder.setRefreshing(false);
        if(orderAdapter.getDatas().size()==0){
            imageViewNothing.setVisibility(View.VISIBLE);
        }else {
            imageViewNothing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
        recyclerViewOder.loadMoreComplete();
//        swipOrder.setRefreshing(false);
        recyclerViewOder.refreshComplete();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
        recyclerViewOder.loadMoreComplete();
//        swipOrder.setRefreshing(false);
        recyclerViewOder.refreshComplete();
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        requsteData();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        requsteData();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == radioOnTheWay.getId()) {
                type = 1 + "";
                pageNo = 1;
                orderAdapter.refreshData(null);
                requsteData();
            } else {
                orderAdapter.refreshData(null);
                type = 2 + "";
                pageNo = 1;
                requsteData();
            }
        }
    }

    @Override
    public void onSuccess(String msg) {
        requsteData();
    }

    @Override
    public void onFailure(PayResult payResult) {
        ToastUtil.toast(payResult.getResult());
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof MyConsultingBean.OBean.DataBean) {
            MyConsultingBean.OBean.DataBean dataBean = (MyConsultingBean.OBean.DataBean) data;
            OrderDetailsActivity.start(getContext(), dataBean);
        }
    }

    public class OrderAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_order;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new OrderHolder(itemView, context, this);
        }

        public class OrderHolder extends RecyclerBaseHolder {
            private CircleImageView circulerHeadOrder;
            TextView textViewOrderTitleName, textViewOrderTime,
                    textViewContent, textViewMoneyOrder;
            Button btnOrderGotoChat;
            private LinearLayout linearlayoutGotoChat;

            public OrderHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                circulerHeadOrder = itemView.findViewById(R.id.circulerHeadOrder);
                textViewOrderTitleName = itemView.findViewById(R.id.textViewOrderTitleName);
                textViewOrderTime = itemView.findViewById(R.id.textViewOrderTime);
                textViewContent = itemView.findViewById(R.id.textViewContent);
                textViewMoneyOrder = itemView.findViewById(R.id.textViewMoneyOrder);
                btnOrderGotoChat = itemView.findViewById(R.id.btnOrderGotoChat);
                linearlayoutGotoChat = itemView.findViewById(R.id.linearlayoutGotoChat);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof MyConsultingBean.OBean.DataBean) {
                    MyConsultingBean.OBean.DataBean bean = (MyConsultingBean.OBean.DataBean) mData;
                    Glide.with(mContext).load(bean.getAvatar()).into(circulerHeadOrder);
                    textViewContent.setText(bean.getProblem());
                    textViewOrderTime.setText(bean.getAddtime());
                    textViewMoneyOrder.setText("¥" + bean.getMoney() + "");
                    textViewOrderTitleName.setText(bean.getTitle());
                    if (bean.getType().equals("2")) {
                        linearlayoutGotoChat.setVisibility(View.GONE);
                    } else {
                        linearlayoutGotoChat.setVisibility(View.VISIBLE);
                    }
                    btnOrderGotoChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Preference.put(ConstantString.OTHER_HEAD, bean.getAvatar());
                            Preference.put(ConstantString.OTHER_NAME, bean.getLawyer());
                            Bundle bundle = new Bundle();
                            bundle.putString(EaseConstant.PRO_ID, bean.getId());
                            bundle.putString(EaseConstant.TOCHAT_NAME, bean.getUser_nicename());
                            bundle.putString(EaseConstant.PROBLEM, bean.getProblem());
                            bundle.putString(EaseConstant.TYPE, type);
                            if(Preference.getBoolean(ConstantString.IS_GROUP)){
                                HyEngine.startConversationGroup(getActivity(), bean.getGroup_id(), bundle, ChatActivity.class);
                            }else {
                                HyEngine.startConversationSingle(MyOrderActivity.this, "ls" + bean.getLawyer(), bundle, ChatActivity.class);
                            }
                        }
                    });
                }

            }
        }
    }

}
