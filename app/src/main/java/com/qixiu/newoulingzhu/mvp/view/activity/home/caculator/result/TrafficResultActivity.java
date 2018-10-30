package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.AddJobFeeActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.AddRaiseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.adapter.ResultAdapter;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.HeadBean;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.RaiseResultBean;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.PopInput;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.TrafficResultBean;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class TrafficResultActivity extends RequstActivity implements OnRecyclerItemListener {
    TrafficResultBean.OBean oBean;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textViewCaculationResultHead)
    TextView textViewCaculationResultHead;
    @BindView(R.id.textViewAddresHead)
    TextView textViewAddresHead;
    @BindView(R.id.textViewLevelHead)
    TextView textViewLevelHead;
    @BindView(R.id.textViewAgeHead)
    TextView textViewAgeHead;
    @BindView(R.id.textViewCashHead)
    TextView textViewCashHead;
    private ResultAdapter adapter;
    HeadBean headBean;
    private TrafficResultBean.OBean.ListBean selecteBean;

    public static void start(Context context, Parcelable headData) {
        Intent intent = new Intent(context, TrafficResultActivity.class);
        intent.putExtra(IntentDataKeyConstant.TITLE, headData);
        context.startActivity(intent);
    }


    @Override
    protected void onInitData() {
        EventBus.getDefault().register(this);
        headBean = getIntent().getParcelableExtra(IntentDataKeyConstant.TITLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        requstData();
    }

    private void requstData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", headBean.getRegistId());
        map.put("level", headBean.getLevelID());
        map.put("aid", headBean.getAddressId());
        map.put("age", headBean.getAge());
        post(ConstantUrl.getTrafficUrl, map, new TrafficResultBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_traffic_result;
    }

    @Override
    protected void onInitViewNew() {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof TrafficResultBean) {
            TrafficResultBean bean = (TrafficResultBean) data;
            oBean = bean.getO();
            setData();
            adapter.setRulsName(oBean.getDocname());
            adapter.setRulesUrl(oBean.getContents());
            for (int j = 0; j < bean.getO().getList().size() - 1; j++) {
                if (bean.getO().getList().get(j).getType().equals("fixed") && !bean.getO().getList().get(j + 1).getType().equals("fixed")) {
                    bean.getO().getList().get(j).setLast(true);
                }
            }
            getTotoalMoney();
        }
    }

    private void setData() {
        textViewAddresHead.setText("地区：" + headBean.getAddress());
        textViewAgeHead.setText("年龄：" + headBean.getAge());
        textViewCashHead.setText("户口：" + headBean.getRegist());
        textViewLevelHead.setText("等级：" + headBean.getLevel());
        mTitleView.setTitle("计算结果");
        textViewAgeHead.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RaiseResultBean bean) {
        for (int i = 0; i < oBean.getList().size(); i++) {
            TrafficResultBean.OBean.ListBean listBean = oBean.getList().get(i);
            if (listBean.getType().equals("url")) {
                listBean.setPrice(bean.getO().getTotal() + "");
                listBean.setIsSelected(true);
                listBean.setoBean(bean.getO());
            }
        }
        adapter.refreshData(oBean.getList());
        getTotoalMoney();
    }

    @Subscribe
    public void onEventMainThread(AddJobFeeActivity.JobMoneyBean bean) {
        selecteBean.setPrice(bean.getMonth() * bean.getWage() + "");
        selecteBean.setIsSelected(true);
        adapter.notifyDataSetChanged();
        getTotoalMoney();
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof TrafficResultBean.OBean.ListBean) {
            /*
            * type : url 表示文章链接地址类型； docname ： 表示文件名称； docurl ： 表示文件地址；
isSelected ： 表示该栏目是否被勾选，true 为勾选，false为不勾选；
"type": "fixed" ， 表示固定项内容； "damages": 60, 表示 伤残或死亡的赔偿金额； "deathburial": 20 表示丧葬赔偿金额；
"type": "time", 表示根据金额和时间来计算金额；
"type": "money", 表示只需要填写金额的项目集合；
"type": "newrequest", 表示需要新开页面去请求的项目；
            * */
            TrafficResultBean.OBean.ListBean listBean = (TrafficResultBean.OBean.ListBean) data;
            selecteBean = listBean;
            if (listBean.getType().equals("url")) {
                AddRaiseActivity.start(getContext(), headBean);
            }
            if (listBean.getType().equals("money") || listBean.getType().equals("time")) {
                setPop(listBean.getTitle() + "总金额");
            }
            if (listBean.getType().equals("months")) {
                AddJobFeeActivity.start(getContext(), listBean.getType());
            }
        }
    }


    //刷新头部总金额的方法
    public void getTotoalMoney() {
        double totoal = 0;
        for (int i = 0; i < oBean.getList().size(); i++) {
            TrafficResultBean.OBean.ListBean listBean = oBean.getList().get(i);
            if (listBean.getPrice() != null) {
                listBean.setPrice((listBean.getPrice() + "元").replace("元元", "元"));
            }
            if ((listBean.getDamages() != 0)) {
                listBean.setPrice(listBean.getDamages() + "元");
            }
            if (!TextUtils.isEmpty(listBean.getDeathburial())) {
                listBean.setPrice(listBean.getDeathburial() + "元");
            }

            try {
                String price = listBean.getPrice().replace("元", "");
                double add = Double.parseDouble(price);
                if (listBean.isIsSelected()) {
                    totoal += add;
                }
            } catch (Exception e) {
            }
        }
        textViewCaculationResultHead.setText(totoal + "");
        adapter.refreshData(oBean.getList());
    }

    public void setPop(String title) {
        PopInput popInput = new PopInput(getContext(), title, new PopInput.ClickListenner() {
            @Override
            public void cancle() {

            }

            @Override
            public void confirm(String input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                selecteBean.setPrice(input);
                selecteBean.setText(input);
                selecteBean.setIsSelected(true);
                adapter.refreshData(oBean.getList());
                getTotoalMoney();
            }
        });
        popInput.setHint("元");
        popInput.show();
    }
}
