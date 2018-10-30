package com.qixiu.newoulingzhu.mvp.view.activity.mine.myorderdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.qixiu.newoulingzhu.beans.messge.ProblemDetailBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.wanchang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppointDetailsActivity extends TitleActivity {


    @BindView(R.id.textxViewTime)
    TextView textxViewTime;
    @BindView(R.id.textxViewAddress)
    TextView textxViewAddress;
    @BindView(R.id.textxViewName)
    TextView textxViewName;
    @BindView(R.id.textxViewPhone)
    TextView textxViewPhone;
    Object data;
    @BindView(R.id.btnGotoCommunicate)
    Button btnGotoCommunicate;
    @BindView(R.id.relativeFinished)
    RelativeLayout relativeFinished;

    public static void start(Context context, Parcelable data) {
        Intent intent = new Intent(context, AppointDetailsActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, data);
        context.startActivity(intent);

    }


    @Override
    protected void onInitData() {
        data = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        if (data instanceof ProblemDetailBean.ProblemDetailInfo) {
            ProblemDetailBean.ProblemDetailInfo bean = (ProblemDetailBean.ProblemDetailInfo) data;
            mTitleView.setTitle(bean.getQustiontype());
            textxViewAddress.setText(bean.getAddress());
            textxViewName.setText(bean.getConnect_user());
            textxViewPhone.setText(bean.getConnect_moble());
            textxViewTime.setText(bean.getMeet_date() + bean.getMeet_time());
            if (bean.getType().equals("1")) {
                relativeFinished.setVisibility(View.GONE);
                btnGotoCommunicate.setVisibility(View.VISIBLE);
            } else {
                relativeFinished.setVisibility(View.VISIBLE);
                btnGotoCommunicate.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_appoint;
    }

    @Override
    protected void onInitViewNew() {

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

    @OnClick(R.id.btnGotoCommunicate)
    public void onViewClicked() {
        if (data instanceof ProblemDetailBean.ProblemDetailInfo) {
            ProblemDetailBean.ProblemDetailInfo bean = (ProblemDetailBean.ProblemDetailInfo) data;
            Preference.put(ConstantString.OTHER_HEAD, bean.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, bean.getLawyer());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, bean.getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, bean.getLawyer());
            bundle.putString(EaseConstant.PROBLEM, bean.getProblem());
            bundle.putString(EaseConstant.TYPE, "1");
            if(Preference.getBoolean(ConstantString.IS_GROUP)){
                HyEngine.startConversationGroup(this,  bean.getGroup_id(), bundle, ChatActivity.class);
            }else {
                HyEngine.startConversationSingle(this, "ls" + bean.getLawyerid(), bundle, ChatActivity.class);
            }
            //
        }

    }
}
