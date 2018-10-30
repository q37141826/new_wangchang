package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.AddRaiseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.InjuryResultBean;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.JobInjuryFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.PopInput;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

public class InjuryResultActivity extends RequstActivity implements OnRecyclerItemListener {
    final int LAST = 10;
    final int FIRST = 11;

    InjuryAdapter adapter = new InjuryAdapter();
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    JobInjuryFragment.DataBean headBean;
    private InjuryResultBean.OBean oBean;

    public static void start(Context context, Parcelable data) {
        Intent intent = new Intent(context, InjuryResultActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("计算结果");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_caculation_result;
    }

    @Override
    protected void onInitViewNew() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        headBean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        textViewAddresHead.setText("地区：" + headBean.getAddress());
        textViewCashHead.setText("工资：" + headBean.getMoney() + "元/月");
        textViewLevelHead.setText("等级：" + headBean.getInjury());
        Map<String, String> map = new HashMap<>();
        map.put("level", headBean.getInjuryId());
        map.put("aid", headBean.getAddressId());
        map.put("money", headBean.getMoney());
        post(ConstantUrl.getInjuryResultUrl, map, new InjuryResultBean());
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof InjuryResultBean) {
            InjuryResultBean bean = (InjuryResultBean) data;
            oBean = bean.getO();
            textViewCaculationResultHead.setText(bean.getO().getTotal() + "");
            bean.getO().getList().get(0).setPosition(FIRST);
            for (int j = 0; j < bean.getO().getList().size() - 1; j++) {
                InjuryResultBean.OBean.ListBean listBean01 = bean.getO().getList().get(j);
                InjuryResultBean.OBean.ListBean listBean02 = bean.getO().getList().get(j + 1);
                if ((listBean01.getType().equals("fixed") && !listBean02.getType().equals("fixed")) && !listBean02.getType().equals("times")) {
                    listBean01.setPosition(LAST);
                    listBean02.setPosition(FIRST);
                }
                if (listBean01.getType().equals("times")) {
                    listBean01.setPosition(LAST);
                    listBean02.setPosition(FIRST);
                }
            }
            adapter.refreshData(bean.getO().getList());
        }
        getTotoalMoney();
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
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof InjuryResultBean.OBean.ListBean) {
            InjuryResultBean.OBean.ListBean listBean = (InjuryResultBean.OBean.ListBean) data;
            if (listBean.getType().equals("url")) {
                AddRaiseActivity.start(getContext(), headBean);
            }
            if (listBean.getType().equals("money")) {
                setPop(listBean);
            }
            if (listBean.getType().equals("time") || listBean.getType().equals("times")) {
                setPopTimes(listBean);
            }
        }
    }

    public void setPopTimes(InjuryResultBean.OBean.ListBean listBean) {
        PopInput popInput = new PopInput(getContext(), "请输入月数", new PopInput.ClickListenner() {
            @Override
            public void cancle() {

            }

            @Override
            public void confirm(String input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                int times = Integer.parseInt(input);
                if (listBean.getSinglePrice() == null) {
                    listBean.setSinglePrice(listBean.getPrice().replace("元/月", "").replace("元", ""));
                }
                double price = Double.parseDouble(listBean.getSinglePrice());
                listBean.setPrice(times * price + "");
                if (listBean.getAllowance() != null) {
                    listBean.setAllowance(times * price + "");
                }
                listBean.setText(times * price + "");
                listBean.setIsSelected(true);
                adapter.notifyDataSetChanged();
                getTotoalMoney();
            }
        });
        popInput.setHint("月数");
        popInput.show();
    }

    public void setPop(InjuryResultBean.OBean.ListBean listBean) {
        PopInput popInput = new PopInput(getContext(), listBean.getTitle()+"总金额", new PopInput.ClickListenner() {
            @Override
            public void cancle() {

            }

            @Override
            public void confirm(String input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                listBean.setPrice(input);
                listBean.setText(input);
                listBean.setIsSelected(true);
                adapter.notifyDataSetChanged();
                getTotoalMoney();
            }
        });
        popInput.setHint("元");
        popInput.show();
    }

    //刷新头部总金额的方法
    public void getTotoalMoney() {
        double totoal = 0;
        for (int i = 0; i < oBean.getList().size(); i++) {
            InjuryResultBean.OBean.ListBean listBean = oBean.getList().get(i);
            try {
                if ((listBean.getDamages()) != null) {
                    listBean.setPrice(listBean.getDamages() + "元");
                }
                if ((listBean.getAllowance()) != null) {
                    listBean.setPrice(listBean.getAllowance() + "元");
                }
                if ((listBean.getDeathburial()) != null) {
                    listBean.setPrice(listBean.getDeathburial() + "元");
                }
                if(listBean.getPrice()==null){
                    listBean.setPrice(listBean.getText());
                }
                String price = listBean.getPrice().replace("元/月", "").replace("元", "");
                double add = Double.parseDouble(price);
                if (listBean.isIsSelected()) {
                    totoal += add;
                }
                if (listBean.getType().contains("time")) {
                    if (listBean.getSinglePrice() == null) {
                        listBean.setSinglePrice(price);
                        listBean.setPrice(price + "元/月");
                    } else {
                        listBean.setPrice(price + "元");
                    }
                } else {
                    listBean.setPrice(price + "元");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        textViewCaculationResultHead.setText(totoal + "");
    }


    public class InjuryAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_injury_result;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new InjuryHolder(itemView, context, this);
        }

        public class InjuryHolder extends RecyclerBaseHolder<InjuryResultBean.OBean.ListBean> {
            View viewTop05Line, viewTop8Line, viewBottom5Line, viewBottom05Line;
            TextView textViewOther, textViewItemTitle, textViewPrice, textViewRulesName;
            ImageView imageViewSelecte, imageViewRightGo;
            LinearLayout linearlayoutInroduce;

            public InjuryHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                viewTop05Line = itemView.findViewById(R.id.viewTop05Line);
                viewTop8Line = itemView.findViewById(R.id.viewTop8Line);
                viewBottom5Line = itemView.findViewById(R.id.viewBottom5Line);
                viewBottom05Line = itemView.findViewById(R.id.viewBottom05Line);
                textViewOther = itemView.findViewById(R.id.textViewOther);
                textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
                textViewPrice = itemView.findViewById(R.id.textViewPrice);
                textViewRulesName = itemView.findViewById(R.id.textViewRulesName);
                imageViewSelecte = itemView.findViewById(R.id.imageViewSelecte);
                imageViewRightGo = itemView.findViewById(R.id.imageViewRightGo);
                linearlayoutInroduce = itemView.findViewById(R.id.linearlayoutInroduce);

            }

            @Override
            public void bindHolder(int position) {
                viewTop8Line.setVisibility(View.GONE);
                viewBottom5Line.setVisibility(View.GONE);
                viewTop05Line.setVisibility(View.GONE);
                viewBottom05Line.setVisibility(View.GONE);
                textViewPrice.setText(mData.getPrice());
                if (mData.getType().equals("fixed")) {//表示后台传入的固定数据，不能改变的
                    imageViewRightGo.setVisibility(View.GONE);
                    textViewOther.setVisibility(View.GONE);
                    if (mData.getPosition() == FIRST) {
                        viewTop8Line.setVisibility(View.VISIBLE);
                        viewBottom05Line.setVisibility(View.VISIBLE);
                    }
                    if (mData.getPosition() == LAST) {
                        viewBottom5Line.setVisibility(View.VISIBLE);
                    }
                } else {
                    imageViewRightGo.setVisibility(View.VISIBLE);
                    viewBottom05Line.setVisibility(View.VISIBLE);
                    if (headBean.getInjuryId().equals("death")) {//根据是否工亡来填写其他项目的抬头
                        textViewOther.setText("亲属抚恤金");
                    } else {
                        textViewOther.setText("生活护理费");
                    }
                    if (mData.getPosition() == FIRST) {
                        textViewOther.setVisibility(View.VISIBLE);
                    } else {
                        textViewOther.setVisibility(View.GONE);
                    }
                    if (TextUtils.isEmpty(mData.getPrice())) {
                        mData.setPrice(mData.getText());
                    }
                    textViewPrice.setText(mData.getPrice());
                }
                textViewItemTitle.setText(mData.getTitle());
                imageViewSelecte.setVisibility(mData.isIsSelected() ? View.VISIBLE : View.INVISIBLE);
                if (position == getDatas().size() - 1) {
                    linearlayoutInroduce.setVisibility(View.VISIBLE);
                    textViewRulesName.setText(oBean.getDocname());
                    linearlayoutInroduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    textViewRulesName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, GoToActivity.class);
                            intent.putExtra(ConstantString.FILEPATH, oBean.getContents());
                            intent.putExtra(ConstantString.TITLE_NAME, oBean.getDocname());
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    linearlayoutInroduce.setVisibility(View.GONE);
                }
            }
        }

    }


}
