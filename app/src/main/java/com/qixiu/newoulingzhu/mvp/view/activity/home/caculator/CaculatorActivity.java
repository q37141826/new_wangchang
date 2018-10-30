package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.BaseFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.JobInjuryFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.LawyerFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.LitigationFragment;
import com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.TrafficFragment;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class CaculatorActivity extends RequstActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.radioJobInjury)
    RadioButton radioJobInjury;
    @BindView(R.id.radioTraffic)
    RadioButton radioTraffic;
    @BindView(R.id.radioLawyear)
    RadioButton radioLawyear;
    @BindView(R.id.radioLitigation)
    RadioButton radioLitigation;
    @BindView(R.id.framlayout_fragment)
    FrameLayout framlayoutFragment;
    List<BaseFragment>  fragments=new ArrayList<>();
    private JobInjuryFragment jobInjuryFragment;
    private TrafficFragment trafficFragment;
    private LawyerFragment lawyerFragment;
    private LitigationFragment litigationFragment;

    @Override
    protected void onInitData() {
        mTitleView.setTitle("法务计算器");
        jobInjuryFragment = new JobInjuryFragment();
        fragments.add(jobInjuryFragment);
        trafficFragment = new TrafficFragment();
        fragments.add(trafficFragment);
        lawyerFragment = new LawyerFragment();
        fragments.add(lawyerFragment);
        litigationFragment = new LitigationFragment();
        fragments.add(litigationFragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            fragmentTransaction.add(R.id.framlayout_fragment, fragments.get(i),
                    fragments.get(i).getClass().getName());
        }
        fragmentTransaction.commit();
        switchFragment(jobInjuryFragment, framlayoutFragment.getId());
        radioJobInjury.setChecked(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_caculator;
    }

    @Override
    protected void onInitViewNew() {
        radioJobInjury.setOnCheckedChangeListener(this);
        radioTraffic.setOnCheckedChangeListener(this);
        radioLawyear.setOnCheckedChangeListener(this);
        radioLitigation.setOnCheckedChangeListener(this);
        setLayout(0);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radioJobInjury:
                    switchFragment(jobInjuryFragment,framlayoutFragment.getId());
                    jobInjuryFragment.reset();
                    break;
                case R.id.radioTraffic:
                    switchFragment(trafficFragment,framlayoutFragment.getId());
                    trafficFragment.reset();
                    break;
                case R.id.radioLawyear:
                    switchFragment(lawyerFragment,framlayoutFragment.getId());
                    lawyerFragment.reset();
                    break;
                case R.id.radioLitigation:
                    switchFragment(litigationFragment,framlayoutFragment.getId());
                    litigationFragment.reset();
                    break;

            }
        }
    }
}
