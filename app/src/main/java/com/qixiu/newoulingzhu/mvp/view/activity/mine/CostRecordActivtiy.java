package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qixiu.newoulingzhu.beans.mine_bean.cost_record.CostRecordBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.RefreshHeader;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.XrecyclerViewUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class CostRecordActivtiy extends RequstActivity implements XRecyclerView.LoadingListener {

    @BindView(R.id.recyclerCostRecord)
    XRecyclerView recyclerCostRecord;
//    @BindView(R.id.swipCostRecord)
//    SwipeRefreshLayout swipCostRecord;
    CostAdapter adapter = new CostAdapter();
    private int pageNo = 1, pageSize = 10;
    //    type ： 表示订单类型，  open 表示会员开通，questions 表示问题购买 ，renew 表示会员续费，upgrade 表示会员升级(二期，会后续添加 类型 )；
    //记录类型// 1、会员开通//2、问题购买//3、合同定制//4、会员升级//5、会员升级（补差价）
    int images[] = {R.mipmap.cost_vip_open2x, R.mipmap.cost_buy2x, R.mipmap.cost_vip_open2x, R.mipmap.cost_vip_open2x, R.mipmap.cost_vip_open2x,R.mipmap.cost_vip_open2x};
    String types[] = {"open", "questions", "renew", "upgrade", "meeting","customized"};
    Map<String, Integer> imageResources = new HashMap<>();

    @Override
    protected void onInitData() {
        mTitleView.setTitle("消费记录");
        for (int i = 0; i < images.length; i++) {
            imageResources.put(types[i], images[i]);
        }
        reqestData();
//        swipCostRecord.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageNo = 1;
//                reqestData();
//            }
//        });
    }

    private void reqestData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        post(ConstantUrl.costRecord, map, new CostRecordBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_cost_record_activtiy;
    }

    @Override
    protected void onInitViewNew() {
        XrecyclerViewUtil.setXrecyclerView(recyclerCostRecord, this, this, true, 1, null);
        recyclerCostRecord.setAdapter(adapter);
//        swipCostRecord.setColorSchemeResources(R.color.theme_color);
        RefreshHeader refreshHeader=new RefreshHeader(getContext());
        refreshHeader.setArrowImageView(R.mipmap.refresh_head_arrow);
        recyclerCostRecord.setRefreshHeader(refreshHeader);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof CostRecordBean) {
            CostRecordBean bean = (CostRecordBean) data;
            if (pageNo == 1) {
                adapter.refreshData(bean.getO().getData());
            } else {
                adapter.addDatas(bean.getO().getData());
            }
        }
//        swipCostRecord.setRefreshing(false);
        recyclerCostRecord.loadMoreComplete();
        recyclerCostRecord.refreshComplete();    }

    @Override
    public void onError(Call call, Exception e, int i) {
//        swipCostRecord.setRefreshing(false);
        super.onError(call,e,i);
        recyclerCostRecord.loadMoreComplete();
        recyclerCostRecord.refreshComplete();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
//        swipCostRecord.setRefreshing(false);
        super.onFailure(c_codeBean);
        recyclerCostRecord.loadMoreComplete();
        recyclerCostRecord.refreshComplete();
    }

    @Override
    public void onRefresh() {
        pageNo=1;
        reqestData();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        reqestData();
    }

    public class CostAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_cost_record;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new CostHolder(itemView, context, this);
        }

        public class CostHolder extends RecyclerBaseHolder {
            private ImageView imageViewIconCost;
            private TextView textViewCostName, textViewCostTime, textViewCostMoney;

            public CostHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                imageViewIconCost = itemView.findViewById(R.id.imageViewIconCost);
                textViewCostName = itemView.findViewById(R.id.textViewCostName);
                textViewCostTime = itemView.findViewById(R.id.textViewCostTime);
                textViewCostMoney = itemView.findViewById(R.id.textViewCostMoney);
            }

            @Override
            public void bindHolder(int position) {
                if (mData instanceof CostRecordBean.OBean.DataBean) {
                    CostRecordBean.OBean.DataBean costRecordBean = (CostRecordBean.OBean.DataBean) mData;
                    try {
                        imageViewIconCost.setImageResource(imageResources.get(costRecordBean.getType()));
                    } catch (Exception e) {
                    }
                    textViewCostName.setText(costRecordBean.getMark());
                    textViewCostTime.setText(costRecordBean.getAdd_time());
                    textViewCostMoney.setText("-" + costRecordBean.getMoney());
                }
            }
        }
    }


}
