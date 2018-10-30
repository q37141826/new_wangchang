package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopAddressLawyer;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopCaseTypeLawyer;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.adapter.SimpleResultAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.ResultBean;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by my on 2018/9/3.
 */

public class LawyerFragment extends BaseFragment implements OKHttpUIUpdataListener {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);
    SimpleResultAdapter adapter = new SimpleResultAdapter();
    @BindView(R.id.linearlayoutResult)
    LinearLayout linearlayoutResult;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewCase)
    TextView textViewCase;
    @BindView(R.id.edttextMoney)
    MyEditTextView edittextMoney;
    @BindView(R.id.btnCalculation)
    Button btnCalculation;
    @BindView(R.id.btnReset)
    Button btnReset;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    PopAddressLawyer popAddress;
    @BindView(R.id.textViewRulesName)
    TextView textViewRulesName;
    private String addressId, caseId;
    PopCaseTypeLawyer popCaseType;

    @Override
    protected void onInitData() {
        popAddress = new PopAddressLawyer(getContext(), new PopAddressLawyer.OnItemClickListenner() {
            @Override
            public void onItemClick(PopAddressLawyer.ProvicesBean.OBean data) {
                textViewAddress.setText(getContext().getString(R.string.please_select_address) + data.getTitle());
                addressId = data.getProvince();
            }
        });
        popCaseType = new PopCaseTypeLawyer(getContext(), new PopCaseTypeLawyer.OnItemClickListenner() {
            @Override
            public void onItemClick(PopCaseTypeLawyer.LawyerCaseBean.OBean data) {
                textViewCase.setText("案件类型：" + data.getTitle());
                caseId = data.getType();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onInitView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.item_fragment_lawyer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.textViewAddress, R.id.textViewCase, R.id.btnCalculation, R.id.btnReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewAddress:
                popAddress.requstPop();
                break;
            case R.id.textViewCase:
                popCaseType.requstPop();
                break;
            case R.id.btnCalculation:
                if (TextUtils.isEmpty(addressId)) {
                    ToastUtil.toast("请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(caseId)) {
                    ToastUtil.toast("请选择案件类型");
                    return;
                }
                if (TextUtils.isEmpty(edittextMoney.getText().toString())) {
                    ToastUtil.toast("请填写诉讼标的");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("province", addressId);
                map.put("type", caseId);
                map.put("money", edittextMoney.getText().toString());
                okHttpRequestModel.okhHttpPost(ConstantUrl.getLawFeeUrl, map, new ResultBean());
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }

    public void reset() {
        try {
            addressId = "";
            caseId = "";
            textViewAddress.setText(R.string.please_select_address);
            textViewCase.setText("案件类型：");
            edittextMoney.setText("");
            linearlayoutResult.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof ResultBean) {
            ResultBean bean = (ResultBean) data;
            adapter.refreshData(bean.getO());
            linearlayoutResult.setVisibility(View.VISIBLE);
           LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
           int lines=0;
            for (int j = 0; j <bean.getO().size() ; j++) {
                if((bean.getO().get(j).getTitle()+bean.getO().get(j).getMoney()).length()>22){
                    lines+=2;
                }else {
                    lines++;
                }
            }
            params.height= ArshowContextUtil.dp2px(23*lines);

            SpannableString spanStr01 = new SpannableString("根据" + bean.getE().getDoc_name() + ":" + "文件计算，结果仅供参考");
            if (!TextUtils.isEmpty(bean.getE().getDoc_name())) {
                spanStr01.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.theme_color)), "根据".length()
                        , ("根据" + bean.getE().getDoc_name() ).length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            textViewRulesName.setText(spanStr01);
            textViewRulesName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoToActivity.class);
                    intent.putExtra(ConstantString.FILEPATH, bean.getE().getContents());
                    intent.putExtra(ConstantString.TITLE_NAME, bean.getE().getDoc_name());
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }

}
