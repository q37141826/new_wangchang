package com.qixiu.newoulingzhu.mvp.view.activity.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.StringConstants;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.qixiu.newoulingzhu.beans.messge.ProblemDetailBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.MainActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.BaseActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.myorderdetails.AppointDetailsActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.myorderdetails.OrderDetailsActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.newoulingzhu.utils.ArshowDialogUtils;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class ChatActivity extends BaseActivity implements EaseChatFragment.OnClickChatFragmentListener, OKHttpUIUpdataListener, ArshowDialogUtils.ArshowDialogListener {

    private EaseChatFragment mChatFragment;

    String toChatUsername;
    private ZProgressHUD mZProgressHUD;
    private OKHttpRequestModel mOkHttpRequestModel;
    private String pro_id;


    @Override
    public void onBackPressed() {
        mChatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (toChatUsername.equals(intent.getStringExtra(EaseConstant.EXTRA_USER_ID)))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }


    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
    }

    @Override
    protected void onInitView() {
        mZProgressHUD = new ZProgressHUD(this);
        mChatFragment = (EaseChatFragment) getSupportFragmentManager().findFragmentByTag(EaseChatFragment.class.getSimpleName());
        if (mChatFragment == null) {
            mChatFragment = new EaseChatFragment();
            mChatFragment.setOnClickFragmentListener(this);
            //传入参数
            mChatFragment.setArguments(getIntent().getExtras());
            addFragment(R.id.fl_content, mChatFragment, EaseChatFragment.class.getSimpleName());
//            MainActivity.unreadMsgCount = 0;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
    }


    @Override
    public void onClickIssueTitle(String pro_id) {
        if (!TextUtils.isEmpty(pro_id)) {
//            Intent intent = new Intent(this, ProblemDetailActivity.class);
//            intent.putExtra(IntentDataKeyConstant.ID, pro_id);
//            startActivityForResult(intent, IntentRequestCodeConstant.REQUESTCODE_START_PROBLEMDETAIL);
            requestData(pro_id);
        }
    }

    private void requestData(String id) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, com.qixiu.newoulingzhu.constant.StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.ID, id);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.ProblemDetialUrl, stringMap, new ProblemDetailBean());
    }

    @Override
    public void onClickComplete(String pro_id) {
        this.pro_id = pro_id;
        if (TextUtils.isEmpty(pro_id)||pro_id.equals("0")) {
            return;
        }
        ArshowDialogUtils.showDialog(this, "关闭后咨询将结束,是否确认关闭问题?", this);

    }

    private void requestComplete(String pro_id) {
        if (mZProgressHUD != null) {
            mZProgressHUD.show();
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
        stringMap.put(IntentDataKeyConstant.ID, pro_id);
        mOkHttpRequestModel.okhHttpPost(ConstantUrl.ProblemDetialCompleteUrl, stringMap, new BaseBean());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentRequestCodeConstant.REQUESTCODE_START_PROBLEMDETAIL &&
                resultCode == IntentRequestCodeConstant.RESULTCODE_START_PROBLEMDETAIL) {

            mChatFragment.setCompleteView(StringConstants.STRING_2);
            mChatFragment.sendForeignTextMessage(Preference.get(ConstantString.TRUE_NAME, StringConstants.EMPTY_STRING));
        }
    }


    @Override
    public void onSuccess(Object data, int i) {
        if(data instanceof ProblemDetailBean){
            ProblemDetailBean bean= ( ProblemDetailBean) data;
            if(bean.getO().getQustiontype()!=null&&bean.getO().getQustiontype().contains("会面咨询")){
                AppointDetailsActivity.start(getContext(),bean.getO());
            }else {
                OrderDetailsActivity.start(getContext(),bean.getO());
            }
        }else {
            if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
                mZProgressHUD.dismiss();
            }
            mChatFragment.sendForeignTextMessage(Preference.get(ConstantString.TRUE_NAME, StringConstants.EMPTY_STRING)+"已结束当前问题");//暂时关闭发送手机号的动作
            mChatFragment.setCompleteView(StringConstants.STRING_2);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("网络异常");

        }
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        if (mZProgressHUD != null && mZProgressHUD.isShowing()) {
            mZProgressHUD.dismissWithFailure("提交失败");

        }
    }

    @Override
    public void onClickPositive(DialogInterface dialogInterface, int which) {
        requestComplete(pro_id);
    }

    @Override
    public void onClickNegative(DialogInterface dialogInterface, int which) {

    }


}
