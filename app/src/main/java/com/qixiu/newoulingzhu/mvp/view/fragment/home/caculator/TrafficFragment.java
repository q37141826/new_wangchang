package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopAddress;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopInjury;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.PopRegist;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.TrafficResultActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.HeadBean;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
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

public class TrafficFragment extends BaseFragment implements OKHttpUIUpdataListener {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);

    @BindView(R.id.textViewAdrees)
    TextView textViewAdrees;
    @BindView(R.id.textViewRegiste)
    TextView textViewRegiste;
    @BindView(R.id.textViewInjury)
    TextView textViewInjury;
    @BindView(R.id.ediitextAge)
    MyEditTextView ediitextAge;
    @BindView(R.id.btnCalculation)
    Button btnCalculation;
    @BindView(R.id.btnReset)
    Button btnReset;
    Unbinder unbinder;
    private PopAddress popAddress;
    private PopInjury popInjury;
    private String addressId, injuryId;
    private PopRegist popRegist;
    private String registeType;
    private String address, level, regist, age;

    @Override
    protected void onInitData() {
        popAddress = new PopAddress(getContext(), new PopAddress.OnItemClickListenner() {
            @Override
            public void onItemClick(PopAddress.ProvicesBean.OBean data) {
                textViewAdrees.setText("选择地区：" + "        " + data.getProvince());
                address = data.getProvince();
                addressId = data.getId();
            }
        });
        popInjury = new PopInjury(getContext(), new PopInjury.OnItemClickListenner() {
            @Override
            public void onItemClick(PopInjury.InjuryBean.OBean data) {
                textViewInjury.setText("伤残等级：" + "        " + data.getName());
                level = data.getName();
                injuryId = data.getType();
            }
        });
        popRegist = new PopRegist(getContext(), new PopRegist.OnItemClickListenner() {
            @Override
            public void onItemClick(PopRegist.RegisteBean data) {
                textViewRegiste.setText("选择户口：" + "        " + data.getName());
                registeType = data.getType();
                regist = data.getName();
            }
        });

    }

    @Override
    protected void onInitView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.item_fragment_traffic;
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

    @OnClick({R.id.textViewAdrees, R.id.textViewRegiste, R.id.textViewInjury, R.id.btnCalculation, R.id.btnReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewAdrees:
                popAddress.requstPop();
                break;
            case R.id.textViewRegiste:
                popRegist.requstPop();
                break;
            case R.id.textViewInjury:
                popInjury.requstPop();
                break;
            case R.id.btnCalculation:
                Map<String, String> map = new HashMap<>();
                if (TextUtils.isEmpty(addressId)) {
                    ToastUtil.toast("请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(injuryId)) {
                    ToastUtil.toast("请选择伤残等级");
                    return;
                }
                if (TextUtils.isEmpty(registeType)) {
                    ToastUtil.toast("请选择户口");
                    return;
                }
                if (TextUtils.isEmpty(ediitextAge.getText().toString())) {
                    ToastUtil.toast("请填写年龄");
                    return;
                }
                HeadBean headBean = new HeadBean(address, addressId, level, injuryId,
                        ediitextAge.getText().toString().trim(), regist, registeType);
                TrafficResultActivity.start(getContext(), headBean);
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }


    public void reset() {
        try {
            addressId = "";
            injuryId = "";
            registeType = "";
            address = "";
            textViewRegiste.setText("选择户口：");
            textViewInjury.setText("伤残等级：");
            textViewAdrees.setText("选择地区：");
            ediitextAge.setText("");
        } catch (Exception e) {
        }

    }

    @Override
    public void onSuccess(Object data, int i) {

    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }


}
