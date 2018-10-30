package com.qixiu.newoulingzhu.mvp.view.activity.mine.edit;

import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.easeui.bean.StringConstants;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.IntentRequestCodeConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CheckStringUtils;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class EditSingleProfileActivity extends TitleActivity implements OKHttpUIUpdataListener {

    private EditText mEt_editsingle;
    private OKHttpRequestModel mOkHttpRequestModel;
    private String mEdit_type;
    String requestUrl;
    private String mContent;
    private MyEditTextView myedittext_change;
    private String name;

    @Override
    protected void onInitViewNew() {
        name = getIntent().getStringExtra(IntentDataKeyConstant.NAME);
        myedittext_change = (MyEditTextView) findViewById(R.id.myedittext_change);
        myedittext_change.setText(name);
        try {
            myedittext_change.setSelection(name.length());
        } catch (Exception e) {
        }
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setRightTextVisible(View.VISIBLE);
        mTitleView.setRightText("完成");

        mEt_editsingle = (EditText) findViewById(R.id.et_editsingle);
        mEdit_type = getIntent().getStringExtra(IntentDataKeyConstant.EDIT_TYPE);
//        if (IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                mEt_editsingle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.my_nickname2x, 0, 0, 0);
//            } else {
//                mEt_editsingle.setCompoundDrawables(getResources().getDrawable(R.mipmap.my_nickname2x), null, null, null);
//            }
//            mTitleView.setTitle("修改昵称");
//            mEt_editsingle.setHint("昵称");
//
//            InputFilter[] filters = {new InputFilter.LengthFilter(11)};
//            mEt_editsingle.setFilters(filters);
//            requestUrl = ConstantUrl.EditVitaNameURl;
//        } else
            if (IntentDataKeyConstant.TRUENAME.equals(mEdit_type)||IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mEt_editsingle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.my_nickname2x, 0, 0, 0);
            } else {
                mEt_editsingle.setCompoundDrawables(getResources().getDrawable(R.mipmap.my_nickname2x), null, null, null);
            }
            mTitleView.setTitle("修改姓名");
            mEt_editsingle.setHint("姓名");
            InputFilter[] filters = {new InputFilter.LengthFilter(8)};
            mEt_editsingle.setFilters(filters);
            requestUrl = ConstantUrl.EditVitaTNameURl;
        } else if (IntentDataKeyConstant.EMAIL.equals(mEdit_type)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mEt_editsingle.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.my_email2x, 0, 0, 0);
            } else {
                mEt_editsingle.setCompoundDrawables(getResources().getDrawable(R.mipmap.my_email2x), null, null, null);
            }
            mTitleView.setTitle("修改邮箱");
            mEt_editsingle.setHint("邮箱");
            requestUrl = ConstantUrl.EditVitaEmliaURl;

        }

        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContent = myedittext_change.getText().toString().trim();
                if (TextUtils.isEmpty(mContent)) {
                    ToastUtil.toast("请输入内容");
                    return;
                }
                if (IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {

                } else if (IntentDataKeyConstant.TRUENAME.equals(mEdit_type)) {

                } else if (IntentDataKeyConstant.EMAIL.equals(mEdit_type)) {
                    if (!CheckStringUtils.isEmail(mContent)) {
                        ToastUtil.toast("邮箱格式有误");
                        return;
                    }

                }
                requestEditSingle();
            }
        });

    }

    private void requestEditSingle() {

        if (TextUtils.isEmpty(requestUrl)) {
            return;
        }


        if (mOkHttpRequestModel == null) {
            mOkHttpRequestModel = new OKHttpRequestModel(this);
        }
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put(IntentDataKeyConstant.UID_KEY, Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
//        if (IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {
//            stringMap.put("nickname", mContent);
//        } else
            if (IntentDataKeyConstant.TRUENAME.equals(mEdit_type)||IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {
            stringMap.put("true_name", mContent);
        } else if (IntentDataKeyConstant.EMAIL.equals(mEdit_type)) {
            stringMap.put("emlia", mContent);
        }

        mOkHttpRequestModel.okhHttpPost(requestUrl, stringMap, new BaseBean());
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onInitData() {
        mOkHttpRequestModel = new OKHttpRequestModel(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_editsingle;
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (IntentDataKeyConstant.NICKNAME.equals(mEdit_type)) {
            Preference.put(ConstantString.NICK_NAME, mContent);
            Preference.put(ConstantString.TRUE_NAME, mContent);
            setResult(IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_NICKNAME);

        } else if (IntentDataKeyConstant.TRUENAME.equals(mEdit_type)) {
            Preference.put(ConstantString.NICK_NAME, mContent);
            Preference.put(ConstantString.TRUE_NAME, mContent);
            setResult(IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_TRUENAME);
        } else {
            Preference.put(ConstantString.EMAIL, mContent);
            setResult(IntentRequestCodeConstant.RESULTCODE_EDITSINGLE_EMAIL);
        }
        finish();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        ToastUtil.toast("网络异常");
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast("修改失败l," + c_codeBean.getM());
    }
}
