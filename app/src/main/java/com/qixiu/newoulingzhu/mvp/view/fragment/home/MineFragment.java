package com.qixiu.newoulingzhu.mvp.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixiu.newoulingzhu.beans.member.MyMemberBean;
import com.qixiu.newoulingzhu.beans.mine_bean.MyCenterBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.ShareLikeEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.CostRecordActivtiy;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.FeedBackActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.MyOrderActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.MyprofileActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.ProblemPaymentActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.SettingActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.view.fragment.base.TitleFragment;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;
import com.qixiu.widget.CircleImageView;
import com.qixiu.widget.LineControllerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by my on 2018/8/13.
 */

public class MineFragment extends TitleFragment implements OKHttpUIUpdataListener {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);

    @BindView(R.id.myProfile)
    LineControllerView myProfile;
    @BindView(R.id.myOrder)
    LineControllerView myOrder;
    @BindView(R.id.questionBuy)
    LineControllerView questionBuy;
    @BindView(R.id.myMember)
    LineControllerView myMember;
    @BindView(R.id.myFeedBack)
    LineControllerView myFeedBack;
    @BindView(R.id.payRecord)
    LineControllerView payRecord;
    @BindView(R.id.aboutUs)
    LineControllerView aboutUs;
    @BindView(R.id.myShare)
    LineControllerView myShare;
    @BindView(R.id.textViewMyMenber)
    TextView textViewMyMenber;
    @BindView(R.id.textViewMemberCode)
    TextView textViewMemberCode;
    @BindView(R.id.textViewLimiteDate)
    TextView textViewLimiteDate;
    @BindView(R.id.textViewQuestionNum)
    TextView textViewQuestionNum;
    Unbinder unbinder;
    @BindView(R.id.circularHeadMine)
    CircleImageView circularHeadMine;
    @BindView(R.id.textViewNicknameMine)
    TextView textViewNicknameMine;
    BroadcastReceiver receiver;

    @Override
    protected void onInitViewNew(View view) {
        //支付成功之后刷新界面
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                requstPersonnalData();
                requstMyMenber();
            }
        };
        IntentFilter intentFilter = new IntentFilter(IntentDataKeyConstant.BROADCAST_WECHAT_PAY_OK);
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("个人中心");
        mTitleView.getLeftView().setVisibility(View.GONE);
        mTitleView.setRightImage(getContext(), R.mipmap.mine_set2x);
        mTitleView.setRightTextVisible(View.VISIBLE);
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.start(getContext());
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }


    @OnClick({R.id.myProfile, R.id.myOrder, R.id.questionBuy, R.id.myMember, R.id.myFeedBack, R.id.payRecord, R.id.aboutUs, R.id.myShare})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.myProfile:
                MyprofileActivity.start(getContext());
                break;
            case R.id.myOrder:
                MyOrderActivity.start(getContext());
                break;
            case R.id.questionBuy:
                ProblemPaymentActivity.start(getContext());
                break;
            case R.id.myMember:
                MyMemberActivity.start(getContext());
                break;
            case R.id.myFeedBack:
                FeedBackActivity.start(getContext());
                break;
            case R.id.payRecord:
                intent = new Intent(getContext(), CostRecordActivtiy.class);
                startActivity(intent);
                break;
            case R.id.aboutUs:
                try {
                    intent = new Intent(getContext(), GoToActivity.class);
                    String url = ConstantUrl.hosturl + "detail/aboutUs.html?URL=" + ConstantUrl.hosturl;
                    intent.putExtra(ConstantString.TITLE_NAME, "关于我们");
                    intent.putExtra(ConstantString.URL, url);
                    startActivity(intent);
                } catch (Exception e) {
                }
                break;
            case R.id.myShare:
                ShareLikeEngine shareLikeEngine = new ShareLikeEngine();
                shareLikeEngine.setShareTitle(ConstantUrl.TITILE);
                shareLikeEngine.releaseShareData(getActivity(), ConstantUrl.SHARE_ICON, ConstantUrl.SHARE_CONTENT, ConstantUrl.SHARE_CLICK_GO_URL, null);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        try {
            unbinder = ButterKnife.bind(this, rootView);
        } catch (Exception e) {

        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        requstPersonnalData();
        requstMyMenber();
    }

    private void requstMyMenber() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        okHttpRequestModel.okhHttpPost(ConstantUrl.myVipUrl, map, new MyMemberBean());
    }

    private void requstPersonnalData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        okHttpRequestModel.okhHttpPost(ConstantUrl.myCenterUrl, map, new MyCenterBean());
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof MyCenterBean) {
            MyCenterBean bean = (MyCenterBean) data;
            Glide.with(getContext()).load(bean.getO().getHead()).error(R.mipmap.login_logo2x).into(circularHeadMine);
            textViewNicknameMine.setText(bean.getO().getTrue_name());
            textViewMyMenber.setText(bean.getO().getName());
            textViewQuestionNum.setText(bean.getO().getCount()); // TODO: 2018/8/24 这个地方后台为什么把问题数量和编号搞反了？？
        }
        if (data instanceof MyMemberBean) {
            MyMemberBean bean = (MyMemberBean) data;
            Glide.with(getContext()).load(bean.getO().getHead()).error(R.mipmap.login_logo2x).into(circularHeadMine);
            textViewLimiteDate.setText(bean.getO().getEtime());
            textViewMemberCode.setText(bean.getO().getNum());
            textViewNicknameMine.setText(bean.getO().getTrue_name());
            Preference.put(ConstantString.LEVEL, bean.getO().getLevel());
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
    }
}
