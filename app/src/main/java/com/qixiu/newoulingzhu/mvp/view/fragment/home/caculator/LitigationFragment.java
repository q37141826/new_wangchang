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
import com.qixiu.newoulingzhu.mvp.view.activity.home.PopIsAsset;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopCaseType;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.adapter.SimpleResultAdapter;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.LawyerCaseBean;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans.ResultBean;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
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

public class LitigationFragment extends BaseFragment implements OKHttpUIUpdataListener {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);
    SimpleResultAdapter adapter = new SimpleResultAdapter();
    @BindView(R.id.textViewCaseType)
    TextView textViewCaseType;
    @BindView(R.id.textViewIsAsseets)
    TextView textViewIsAsseets;
    @BindView(R.id.ediitextMoney)
    MyEditTextView ediitextMoney;
    @BindView(R.id.btnCalculation)
    Button btnCalculation;
    @BindView(R.id.btnReset)
    Button btnReset;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.linearlayoutResult)
    LinearLayout linearlayoutResult;
    @BindView(R.id.textViewRulesName)
    TextView textViewRulesName;

    private String caseType;
    private PopCaseType popCaseType;
    private boolean is_asset = false;
    private PopIsAsset popInput;
    private boolean isCheck;
    @Override
    protected void onInitData() {
        popCaseType = new PopCaseType(getContext(), new PopCaseType.OnItemClickListenner() {
            @Override
            public void onItemClick(LawyerCaseBean.OBean data) {
                caseType = data.getType();
                textViewCaseType.setText("案件类型：" + data.getTitle());
                is_asset = data.isIs_asset();
                isCheck = data.isCheck();
                if (isCheck) {
                    textViewIsAsseets.setText("涉及财产：" + "是");
                    ediitextMoney.setEnabled(true);
                } else {
                    textViewIsAsseets.setText("涉及财产：" + "否");
                    ediitextMoney.setEnabled(false);
                    ediitextMoney.setText("");
                }

            }
        });
        popInput = new PopIsAsset(getContext(), new PopIsAsset.OnItemClickListenner() {
            @Override
            public void onItemClick(PopIsAsset.CaseType data) {
                ediitextMoney.setEnabled(data.isAsset());
                textViewIsAsseets.setText("涉及财产：" + data.getName());
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
        return R.layout.item_fragment_litigation;
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

    @OnClick({R.id.textViewCaseType, R.id.textViewIsAsseets, R.id.btnCalculation, R.id.btnReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewCaseType:
                popCaseType.requstPop();
                break;
            case R.id.textViewIsAsseets:
                if (!is_asset) {
                    popInput.requstPop();
                }
                break;
            case R.id.btnCalculation:
                if (TextUtils.isEmpty(caseType)) {
                    ToastUtil.toast("请选择案件类型");
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("type", caseType);
                CommonUtils.putDataIntoMap(map, "is_asset", textViewIsAsseets.getText().toString().contains("是") ? "1" : "0");
                CommonUtils.putDataIntoMap(map, "money", ediitextMoney.getText().toString());
                okHttpRequestModel.okhHttpPost(ConstantUrl.getlitigationUrl, map, new ResultBean());
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }


    public void reset() {
        try {
            caseType = "";
            is_asset = false;
            ediitextMoney.setText("");
            textViewCaseType.setText("案件类型：");
            textViewIsAsseets.setText("涉及财产：");
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
                if((bean.getO().get(j).getTitle()+bean.getO().get(j).getMoney()).length()>17){
                    lines+=2;
                }else {
                    lines++;
                }
            }
            params.height= ArshowContextUtil.dp2px(24*lines);
            SpannableString spanStr01 = new SpannableString("根据"  +bean.getE().getDoc_name()+ ":" + "文件计算，结果仅供参考");
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
