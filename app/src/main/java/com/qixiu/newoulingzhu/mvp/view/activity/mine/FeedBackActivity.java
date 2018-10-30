package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.view.activity.base.upload.UploadPictureActivityNew;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.IntelligentTextWatcher;
import com.qixiu.wigit.myedittext.TextWatcherAdapterInterface;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeedBackActivity extends UploadPictureActivityNew implements TextWatcherAdapterInterface, CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.recyclerFeedBack)
    RecyclerView recyclerFeedBack;
    @BindView(R.id.readioGroupFeedback)
    RadioGroup readioGroupFeedback;
    @BindView(R.id.edittextFeedbackPhone)
    EditText edittextFeedbackPhone;
    @BindView(R.id.radio01)
    RadioButton radio01;
    @BindView(R.id.radio02)
    RadioButton radio02;
    @BindView(R.id.radio03)
    RadioButton radio03;
    private EditText edittext_feed_back;
    private TextView textView_feedback_state;
    private Button btn_confirm_feedback;
    private OKHttpRequestModel okHttpRequestModel;
    private String type;//	反馈类型： question 问题咨询，opinion 意见和建议，other 其它

    private void initClick() {
        btn_confirm_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_feedback:
                String text = edittext_feed_back.getText().toString();
                if (TextUtils.isEmpty(type)) {
                    ToastUtil.showToast(this, "请选择分类");
                    return;
                }
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showToast(this, "请填写反馈内容");
                    return;
                }
                if (TextUtils.isEmpty(edittextFeedbackPhone.getText().toString().trim())) {
                    ToastUtil.showToast(this, "请填写手机号码");
                    return;
                }
                if (!CommonUtils.isMobileNO(edittextFeedbackPhone.getText().toString().trim())) {
                    ToastUtil.showToast(this, R.string.edittext_login_input_id_input_rule_input_illegal);
                    return;
                }
                startFeedBack(text);
                break;
        }
    }

    private void startFeedBack(String text) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("content", text);
        map.put("telphone", edittextFeedbackPhone.getText().toString().trim());
        CommonUtils.putDataIntoMap(map, "type", type);
        setImageKey("image");
        requestUpLoad(ConstantUrl.feedBackUrl, map);
    }

    @Override
    protected void onInitData() {
        mTitleView.setTitle("意见反馈");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        radio01.setOnCheckedChangeListener(this);
        radio02.setOnCheckedChangeListener(this);
        radio03.setOnCheckedChangeListener(this);
        if(!hasPermission(photoPermission)){
            hasRequse(1,photoPermission);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_feed_back;
    }


    @Override
    public void beforeTextChanged(int editTextId, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(int editTextId, CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(int editTextId, Editable s) {
        int length = edittext_feed_back.getText().toString().length();
        textView_feedback_state.setText(length + "/1000");
    }


    @Override
    protected void onUpLoad(Object data) {

    }

    @Override
    public void initUpLoadView() {
        okHttpRequestModel = new OKHttpRequestModel(this);
        edittext_feed_back = (EditText) findViewById(R.id.edittext_feed_back);
        textView_feedback_state = (TextView) findViewById(R.id.textView_feedback_state);
        btn_confirm_feedback = (Button) findViewById(R.id.btn_confirm_feedback);
        initClick();
        IntelligentTextWatcher textwatech = new IntelligentTextWatcher(getContext(), edittext_feed_back.getId(), this);
        textwatech.setEmojiDisabled(true, edittext_feed_back);
        edittext_feed_back.addTextChangedListener(textwatech);
        recyclerFeedBack.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerFeedBack;
    }

    @Override
    protected void onInitViewNew() {
        setMaxPictureCount(9);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == radio01.getId() && isChecked) {
            type = "question";//	反馈类型： question 问题咨询，opinion 意见和建议，other 其它
        }
        if (buttonView.getId() == radio02.getId() && isChecked) {
            type = "opinion";
        }
        if (buttonView.getId() == radio03.getId() && isChecked) {
            type = "other";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!hasPermission(photoPermission)){
            finish();
        }
    }
}
