package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.wanchang.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddJobFeeActivity extends TitleActivity {


    @BindView(R.id.editextWages)
    EditText editextWages;
    @BindView(R.id.editextNum)
    EditText editextNum;
    @BindView(R.id.btnStartCaculate)
    Button btnStartCaculate;

    public static void start(Context context, String type) {
        Intent intent = new Intent(context, AddJobFeeActivity.class);
        intent.putExtra(IntentDataKeyConstant.TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("误工费");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_job_fee;
    }

    @Override
    protected void onInitViewNew() {
        String type = getIntent().getStringExtra(IntentDataKeyConstant.TYPE);
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

    @OnClick(R.id.btnStartCaculate)
    public void onViewClicked() {
        EventBus.getDefault().post(new JobMoneyBean(Integer.parseInt(TextUtils.isEmpty(editextNum.getText().toString())?"0":
                editextNum.getText().toString()), (Integer.parseInt(TextUtils.isEmpty(editextWages.getText().toString())?"0":editextWages.getText().toString()))));
        finish();
    }

    public class JobMoneyBean {
        int month;
        int wage;

        public JobMoneyBean(int month, int wage) {
            this.month = month;
            this.wage = wage;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getWage() {
            return wage;
        }

        public void setWage(int wage) {
            this.wage = wage;
        }
    }

}
