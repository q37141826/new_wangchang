package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.HeadBean;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.RaiseBean;
import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.RaiseResultBean;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.recyclerview_lib.RecyclerBaseAdapter;
import com.qixiu.recyclerview_lib.RecyclerBaseHolder;
import com.qixiu.wanchang.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AddRaiseActivity extends RequstActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    AddAdapter addAdapter = new AddAdapter();
    List<RaiseBean> datas = new ArrayList<>();
    @BindView(R.id.btnStartCaculate)
    Button btnStartCaculate;
    HeadBean headBean;

    @Override
    public void adustTitle() {
//        super.adustTitle();
    }

    public static void start(Context context, Parcelable datas) {
        Intent intent = new Intent(context, AddRaiseActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, datas);
        context.startActivity(intent);
    }


    @Override
    protected void onInitData() {
        headBean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(addAdapter);
        addAdapter.refreshData(datas);
        mTitleView.setTitle("抚养费计算");
        mTitleView.setRightText("＋");
        mTitleView.getRightText().setTextSize(20);
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });
    }

    private void addItem() {
        RaiseBean raiseBean = new RaiseBean("", "");
        datas.add(raiseBean);
        addAdapter.refreshData(datas);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_raise;
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

    @OnClick(R.id.btnStartCaculate)
    public void onViewClicked() {
        if (datas.size() == 0) {
            finish();
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            if (TextUtils.isEmpty(datas.get(i).getAge())) {
                ToastUtil.toast("请输入被抚养人年龄");
                return;
            }
            if (TextUtils.isEmpty(datas.get(i).getPerson())) {
                ToastUtil.toast("请输入承担抚养义务的人数");
                return;
            }
        }
        String json = new Gson().toJson(datas);
        Log.e("json", json);
        Map<String, String> map = new HashMap<>();
        map.put("aid", headBean.getAddressId());
        map.put("type", headBean.getRegistId());
        map.put("level", headBean.getLevelID());
        map.put("personage", json);
        post(ConstantUrl.getRaiseUrl, map, new RaiseResultBean());

    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof RaiseResultBean) {
            EventBus.getDefault().post(data);
            finish();
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
    }


    public class AddAdapter extends RecyclerBaseAdapter {
        @Override
        public int getLayoutId() {
            return R.layout.item_add_caculator;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new AddHolder(itemView, context, this);
        }

        public class AddHolder extends RecyclerBaseHolder<RaiseBean> {
            private EditText editextAge;
            private EditText editextNum;

            public AddHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                editextAge = itemView.findViewById(R.id.editextAge);
                editextNum = itemView.findViewById(R.id.editextNum);
            }

            @Override
            public void bindHolder(int position) {
                editextAge.setText(mData.getAge());
                editextNum.setText(mData.getPerson());
                editextAge.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mData.setAge(s.toString());
                    }
                });
                editextNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mData.setPerson(s.toString());
                    }
                });

            }
        }
    }


}
