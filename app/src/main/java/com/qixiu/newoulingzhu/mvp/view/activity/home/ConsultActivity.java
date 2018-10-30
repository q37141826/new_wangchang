package com.qixiu.newoulingzhu.mvp.view.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.qixiu.newoulingzhu.beans.home.consult.ConsultBean;
import com.qixiu.newoulingzhu.beans.home.consult.ConsultingInroductionBean;
import com.qixiu.newoulingzhu.beans.home.consult.ProblemGotoPayBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.upload.UploadPictureActivityNew;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.ProblemPaymentActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.decalaration.LinearSpacesItemDecoration;
import com.qixiu.wigit.myedittext.IntelligentTextWatcher;
import com.qixiu.wigit.myedittext.TextWatcherAdapterInterface;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.qixiu.newoulingzhu.constant.ConstantUrl.countMessageUrl;


/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class ConsultActivity extends UploadPictureActivityNew implements TextWatcherAdapterInterface {
    @BindView(R.id.textView_times_can_consulting)
    TextView textView_times_can_consulting;

    @BindView(R.id.webviewIntroduce)
    WebView webviewIntroduce;
    @BindView(R.id.btnStudy)
    Button btnStudy;


    private RecyclerView mRv_answers;

    private EditText mEt_answers_content;
    private Button mBt_release;
    private IntelligentTextWatcher mIntelligentTextWatcher;
    private IntelligentTextWatcher mIntelligentTextWatcher2;
    private OKHttpRequestModel okHttpRequestModel;

    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data instanceof ProblemGotoPayBean) {
            ProblemGotoPayBean problemGotoPayBean = (ProblemGotoPayBean) data;
            int times = (int) Double.parseDouble(problemGotoPayBean.getO().toString());
            textView_times_can_consulting.setText(times + "");
            if (times == 0) {
                setDialog("不好意思，请先开通会员或购买问题，我们将及时为您服务");
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(IntentDataKeyConstant.NOTIFY_ANSWERCIRCLE_RELEASESUCCESS_ACTION);
            sendBroadcast(intent);
            super.onSuccess(data, i);
        }
        if (data instanceof ConsultingInroductionBean) {
            ConsultingInroductionBean bean = (ConsultingInroductionBean) data;
            CommonUtils.setWebview(webviewIntroduce, bean.getO().getCount(), true);
        }
        if (!hasPermission(photoPermission)) {
            hasRequse(1, photoPermission);
        }

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
        if (!c_codeBean.getM().contains("律师")) {
            setDialog("不好意思，请先开通会员或购买问题，我们将及时为您服务");
        }
    }

    @Override
    protected void onInitData() {
        setImageKey("img");
        setUploadbean(new ConsultBean());
        textView_times_can_consulting.setFocusable(true);
        textView_times_can_consulting.requestFocus();
        okHttpRequestModel = new OKHttpRequestModel(this);
        getBuyData();
        getIntroduction();
        mEt_answers_content.setText(Preference.get(ConstantString.INPUT, ""));
        mEt_answers_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Preference.put(ConstantString.INPUT, mEt_answers_content.getText().toString());
            }
        });
        CommonUtils.hiddeKeybord(mEt_answers_content, this);
    }

    public void getBuyData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        ProblemGotoPayBean bean = new ProblemGotoPayBean();
        okHttpRequestModel.okhHttpPost(ConstantUrl.problemBuyUrl, map, bean);
    }

    @Override
    protected void onUpLoad(Object data) {
        if (data instanceof ConsultBean) {
            ConsultBean consultBean = (ConsultBean) data;
            ConsultBean.ConsultInfo consultInfo = consultBean.getE();
            Preference.put(ConstantString.OTHER_HEAD, consultInfo.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, consultInfo.getUser_nicename());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, consultBean.getO().getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, consultInfo.getUser_nicename());
            bundle.putString(EaseConstant.PROBLEM, mEt_answers_content.getText().toString().trim());
            bundle.putString(EaseConstant.TYPE, StringConstants.STRING_1);
            if (Preference.getBoolean(ConstantString.IS_GROUP)) {
                HyEngine.startConversationGroup(this, consultBean.getO().getGroup_id(), bundle, ChatActivity.class);//todo 这里替换成群聊
            } else {
                HyEngine.startConversationSingle(this, "ls" + consultInfo.getId(), bundle, ChatActivity.class);
            }
        }
    }


    @Override
    protected void onDestroy() {

        if (mEt_answers_content != null && mIntelligentTextWatcher2 != null) {
            mEt_answers_content.removeTextChangedListener(mIntelligentTextWatcher2);
        }
        super.onDestroy();
    }


    @Override
    public void initUpLoadView() {
        mTitleView.setTitle("我要咨询");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEt_answers_content = (EditText) findViewById(R.id.et_answers_content);
        mBt_release = (Button) findViewById(R.id.bt_release);

        mRv_answers = (RecyclerView) findViewById(R.id.rv_answers);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRv_answers.addItemDecoration(new LinearSpacesItemDecoration(LinearLayoutManager.VERTICAL, ArshowContextUtil.dp2px(10)));
        mRv_answers.setLayoutManager(gridLayoutManager);
        mBt_release.setOnClickListener(this);

        mIntelligentTextWatcher2 = new IntelligentTextWatcher(this, mEt_answers_content.getId(), this);
        mIntelligentTextWatcher2.setEmojiDisabled(true, mEt_answers_content);
        mEt_answers_content.addTextChangedListener(mIntelligentTextWatcher2);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRv_answers;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_consult;
    }


    @Override
    public void beforeTextChanged(int editTextId, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(int editTextId, CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(int editTextId, Editable s) {

    }

    @Override
    protected void onInitViewNew() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_release:
                if (TextUtils.isEmpty(mEt_answers_content.getText().toString())) {
                    ToastUtil.toast("请输入问题描述");
                    return;
                }
                setDialogNew("确认是否提交");
                break;

        }
    }

    private void setDialogNew(String message) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startUpload();
                dialog.dismiss();
            }
        });

        builder.setMessage(message);
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void startUpload() {
        String answersContent = mEt_answers_content.getText().toString();
        if (TextUtils.isEmpty(answersContent.trim())) {
            ToastUtil.toast("请输入问题描述");
            return;
        }
        mZProgressHUD.show();
        Map<String, String> mMap = new HashMap<>();
        mMap.put("problem", answersContent.trim());
        requestUpLoad(ConstantUrl.ConsultURl, mMap);
        Preference.put(ConstantString.INPUT, "");

    }


    private void setDialog(String message) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                intent = new Intent(ConsultActivity.this, MyMemberActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setMessage(message);
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("购买问题", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                intent = new Intent(ConsultActivity.this, ProblemPaymentActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public void getIntroduction() {
        Map<String, String> map = new HashMap<>();
        map.put("id", 9 + "");
        okHttpRequestModel.okhHttpPost(countMessageUrl, map, new ConsultingInroductionBean());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnStudy)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), GoToActivity.class);
        intent.putExtra(ConstantString.URL, ConstantUrl.studyConsult);
        intent.putExtra(ConstantString.TITLE_NAME, "操作流程");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!hasPermission(photoPermission)) {
            finish();
        }
    }
}
