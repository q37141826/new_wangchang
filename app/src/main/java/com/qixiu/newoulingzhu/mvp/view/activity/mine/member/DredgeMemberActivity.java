package com.qixiu.newoulingzhu.mvp.view.activity.mine.member;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.beans.mine_bean.member.DredgeMemberBean;
import com.qixiu.newoulingzhu.beans.mine_bean.member.DredgeMemberPriceBean;
import com.qixiu.newoulingzhu.beans.mine_bean.member.DredgeMemberPromptPayBean;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.pay.AliWeixinPayActivity;
import com.qixiu.newoulingzhu.mvp.view.adapter.mine.member.DredgeMemberAdapter;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.OnRecyclerItemListener;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class DredgeMemberActivity extends TitleActivity implements OKHttpUIUpdataListener, OnRecyclerItemListener {

    private RecyclerView mRv_dredge_member;
    private TextView mTv_member_year_price;
    private TextView mTv_time_limit;
    private TextView mTv_totalprice;
    private Button mBt_pay;
    private OKHttpRequestModel mOkHttpRequestModel;
    private DredgeMemberAdapter mDredgeMemberAdapter;
    private String mType;
    private DredgeMemberPriceBean.DredgeMemberPriceInfo mDredgeMemberPriceInfo;
    private String mMemberId;

    @Override
    protected void onInitViewNew() {

        mType = getIntent().getStringExtra(IntentDataKeyConstant.TYPE);
        if (StringConstants.STRING_1.equals(mType)) {
            mTitleView.setTitle("开通会员");
        } else if (StringConstants.STRING_2.equals(mType)) {
            mTitleView.setTitle("升级会员");
        } else {
            mTitleView.setTitle("续费会员");
        }

        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRv_dredge_member = (RecyclerView) findViewById(R.id.rv_dredge_member);

        mTv_member_year_price = (TextView) findViewById(R.id.tv_member_year_price);
        mTv_time_limit = (TextView) findViewById(R.id.tv_time_limit);
        mTv_totalprice = (TextView) findViewById(R.id.tv_totalprice);
        mBt_pay = (Button) findViewById(R.id.bt_pay);
        mBt_pay.setOnClickListener(this);

        mDredgeMemberAdapter = new DredgeMemberAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv_dredge_member.setLayoutManager(linearLayoutManager);
        mRv_dredge_member.setAdapter(mDredgeMemberAdapter);
        mDredgeMemberAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pay:
                if (mDredgeMemberPriceInfo != null) {
                    if (!showFailureToast(mDredgeMemberPriceInfo.getDate())) {
                        return;
                    }
                    requestPromptlyPay();

                }
                break;

        }
    }

    private void requestPromptlyPay() {
        if (TextUtils.isEmpty(mMemberId)) {
            return;
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put("vset_id", mMemberId);
        stringMap.put("money", mDredgeMemberPriceInfo.getMoney());
        stringMap.put("num", StringConstants.STRING_1);
        if (StringConstants.STRING_3.equals(mType)) {
            stringMap.put("renew", StringConstants.STRING_1);
        } else if (StringConstants.STRING_1.equals(mType)) {
            stringMap.put("renew", StringConstants.EMPTY_STRING);
        } else {
            stringMap.put("renew", StringConstants.STRING_2);
        }

        mOkHttpRequestModel.okhHttpPost(ConstantUrl.promptlyPayUrl, stringMap, new DredgeMemberPromptPayBean());
    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
        requestMemberData();

    }

    private void requestMemberData() {

        mOkHttpRequestModel.okhHttpPost(ConstantUrl.dredgeVipUrl, null, new DredgeMemberBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dredge_member;
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof DredgeMemberBean) {
            DredgeMemberBean dredgeMemberBean = (DredgeMemberBean) data;
            List<DredgeMemberBean.DredgeMemberInfo> dredgeMemberInfos = dredgeMemberBean.getO();
            if (dredgeMemberInfos.size() > 0) {
                DredgeMemberBean.DredgeMemberInfo dredgeMemberInfo = dredgeMemberInfos.get(0);
                dredgeMemberInfo.setSelected(true);
                String url;
                if (StringConstants.STRING_2.equals(mType)) {
                    url = ConstantUrl.upgradeVipPriceUrl;
                } else {
                    url = ConstantUrl.dredgeVipPriceUrl;

                }
                mMemberId = dredgeMemberInfo.getId();
                requestMemerPrice(url, dredgeMemberInfo.getId());
            }
            mDredgeMemberAdapter.refreshData(dredgeMemberInfos);


        } else if (data instanceof DredgeMemberPriceBean) {
            DredgeMemberPriceBean dredgeMemberPriceBean = (DredgeMemberPriceBean) data;
            mDredgeMemberPriceInfo = dredgeMemberPriceBean.getO();
            mTv_member_year_price.setText(mDredgeMemberPriceInfo.getMoney());
            mTv_time_limit.setText(mDredgeMemberPriceInfo.getDate());
            mTv_totalprice.setText(mDredgeMemberPriceInfo.getMoney());
            showFailureToast(mDredgeMemberPriceInfo.getDate());

        } else if (data instanceof DredgeMemberPromptPayBean) {
            DredgeMemberPromptPayBean dredgeMemberPromptPayBean = (DredgeMemberPromptPayBean) data;
            DredgeMemberPromptPayBean.DredgeMemberPromptPayInfo dredgeMemberPromptPayInfo = dredgeMemberPromptPayBean.getO();
            Intent intent = new Intent(this, AliWeixinPayActivity.class);
            intent.putExtra(IntentDataKeyConstant.ID, dredgeMemberPromptPayInfo.getId());
            intent.putExtra(IntentDataKeyConstant.ORDER_ID, dredgeMemberPromptPayInfo.getOrder());
            intent.putExtra(IntentDataKeyConstant.MONEY, dredgeMemberPromptPayInfo.getMoney());
            startActivity(intent);
        }
    }

    public boolean showFailureToast(String date) {
        if (StringConstants.STRING_2.equals(mType)) {
            if ("暂无".equals(date)) {
                ToastUtil.toast("不能升级同等级和低于自身等级的会员");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        ToastUtil.toast(getString(R.string.not_netwroking));
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
    }

    @Override
    public void onItemClick(View v, RecyclerView.Adapter adapter, Object data) {
        if (data instanceof DredgeMemberBean.DredgeMemberInfo) {
            DredgeMemberBean.DredgeMemberInfo dredgeMemberInfo = (DredgeMemberBean.DredgeMemberInfo) data;
            if (dredgeMemberInfo.isSelected()) {
                return;
            }
            dredgeMemberInfo.setSelected(true);
            List<DredgeMemberBean.DredgeMemberInfo> dredgeMemberInfos = mDredgeMemberAdapter.getDatas();

            for (int i = 0; i < dredgeMemberInfos.size(); i++) {
                if (dredgeMemberInfo != dredgeMemberInfos.get(i)) {
                    dredgeMemberInfos.get(i).setSelected(false);
                }

            }
            Glide.with(this).load(R.mipmap.member_select).skipMemoryCache(false).into((ImageView) v.findViewById(R.id.iv_select_icon));
            for (int i = 0; i < mRv_dredge_member.getChildCount(); i++) {
                if (v != mRv_dredge_member.getChildAt(i)) {
                    Glide.with(this).load(R.mipmap.member_not_select).skipMemoryCache(false).into((ImageView) (mRv_dredge_member.getChildAt(i).findViewById(R.id.iv_select_icon)));
                }
            }
            String url = null;
            if (StringConstants.STRING_2.equals(mType)) {
                url = ConstantUrl.upgradeVipPriceUrl;
            } else {
                url = ConstantUrl.dredgeVipPriceUrl;

            }
            mMemberId = dredgeMemberInfo.getId();
            requestMemerPrice(url, mMemberId);
        }

    }

    private void requestMemerPrice(String url, String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.ID, id);
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        mOkHttpRequestModel.okhHttpPost(url, stringMap, new DredgeMemberPriceBean());
    }
}
