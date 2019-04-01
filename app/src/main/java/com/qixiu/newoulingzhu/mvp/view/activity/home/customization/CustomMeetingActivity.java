package com.qixiu.newoulingzhu.mvp.view.activity.home.customization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.Alipay;
import com.alipay.PayResult;
import com.alipay.interf.AliPayBean;
import com.alipay.interf.IPay;
import com.hyphenate.easeui.EaseConstant;
import com.qixiu.newoulingzhu.application.LoginStatus;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;
import com.qixiu.newoulingzhu.constant.StringConstants;
import com.qixiu.newoulingzhu.engine.HyEngine;
import com.qixiu.newoulingzhu.mvp.view.activity.base.GoToActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.base.RequstActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.chat.ChatActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans.CustomInnerBean;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans.LawyerBean;
import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans.PriceBean;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.MyprofileActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.qixiu.utils.TimeDataUtil;
import com.qixiu.wanchang.R;
import com.qixiu.wanchang.wxapi.WeiChatInstallCheckUtils;
import com.qixiu.wanchang.wxapi.WeixinPayModel;
import com.qixiu.wigit.myedittext.MyEditTextView;
import com.qixiu.wigit.picker.AddressBean;
import com.qixiu.wigit.picker.MyAddressInitTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class CustomMeetingActivity extends RequstActivity implements IPay, CompoundButton.OnCheckedChangeListener, MyAddressInitTask.PickListene {
    String payType = "meeting";

    @BindView(R.id.tomy_right)
    TextView tomyRight;
    @BindView(R.id.textView_times_can_consulting)
    TextView textViewTimesCanConsulting;
    @BindView(R.id.btn_gotoMember)
    Button btnGotoMember;
    @BindView(R.id.tomyright)
    ImageView tomyright;
    @BindView(R.id.btnStudy)
    Button btnStudy;
    @BindView(R.id.imageViewIntroduce)
    ImageView tomyright01;
    @BindView(R.id.tv_dymaic_title_txt)
    TextView tvDymaicTitleTxt;
    @BindView(R.id.tv_attention_one)
    TextView tvAttentionOne;
    @BindView(R.id.tv_attention_two)
    TextView tvAttentionTwo;
    @BindView(R.id.tv_attention_three)
    TextView tvAttentionThree;
    @BindView(R.id.tv_dymaic_content_txt)
    TextView tvDymaicContentTxt;
    @BindView(R.id.calenderview)
    CalendarView calenderview;
    @BindView(R.id.radio_morning)
    RadioButton radioMorning;
    @BindView(R.id.radio_afterloon)
    RadioButton radioAfterloon;
    @BindView(R.id.textViewAddressSelect)
    TextView textViewAddressSelect;
    @BindView(R.id.edittextContractName)
    MyEditTextView edittextContractName;
    @BindView(R.id.edittextContractPhone)
    MyEditTextView edittextContractPhone;
    @BindView(R.id.webviewIntroduce)
    TextView webviewIntroduce;
    @BindView(R.id.bt_release)
    Button btRelease;
    CustomizationListActivity.CustomItemBean.OBean bean;
    private PriceBean priceBean;
    private String paymethod = "1";
    private String orderId;
    BroadcastReceiver receiver;
    private CustomizationListActivity.CustomItemBean.OBean customItemBean;
    private View contentView;
    private ImageView imageViewWeichatSelect;
    private ImageView imageViewAliSelect;
    private PopupWindow popupWindow;
    private String time;//上午还是下午
    private String user;
    private String mobile;
    private String dateString;
    private Button btnPaySoon;

    @Override
    public void adustTitle() {
//        super.adustTitle();
    }

    public static void start(Context context, CustomizationListActivity.CustomItemBean.OBean bean) {
        Intent intent = new Intent(context, CustomMeetingActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, bean);
        context.startActivity(intent);
    }


    @Override
    protected void onInitData() {
        bean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        mTitleView.setTitle(bean.getTitle());
        radioMorning.setChecked(true);
        calenderview.setMinDate(new Date().getTime() + 12 * 3600 * 1000);
        calenderview.setDate(new Date().getTime() + 12 * 3600 * 1000);
        customItemBean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        requstData();
        requestPrice();
        //微信支付监听
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onSuccess("");
            }
        };
        IntentFilter intentFilter = new IntentFilter(IntentDataKeyConstant.BROADCAST_WECHAT_PAY_OK);
        registerReceiver(receiver, intentFilter);
    }

    private void reqestAddress() {
        Map<String,String> map=new HashMap<>();
        get(ConstantUrl.getAddressUrl,map,new AddressBean());
    }

    private void requestPrice() {
        Map<String, String> map = new HashMap<>();
        map.put("type", customItemBean.getType());
        map.put("uid", LoginStatus.getToken());
        post(ConstantUrl.getOrderPriceUrl, map, new PriceBean());

    }

    private void requstData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", customItemBean.getType());
        get(ConstantUrl.customWebUrl, map, new CustomInnerBean());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_custom_meeting;
    }

    @Override
    protected void onInitViewNew() {
        btRelease.setOnClickListener(this);
        textViewAddressSelect.setOnClickListener(this);
        radioMorning.setOnCheckedChangeListener(this);
        radioAfterloon.setOnCheckedChangeListener(this);
        btnStudy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_weichat_pop:
                imageViewWeichatSelect.setImageResource(R.mipmap.select2x);
                imageViewAliSelect.setImageResource(R.mipmap.no_selected2x);
                paymethod = "1";
                break;
            case R.id.relative_alipay_pop:
                imageViewWeichatSelect.setImageResource(R.mipmap.no_selected2x);
                imageViewAliSelect.setImageResource(R.mipmap.select2x);
                paymethod = "2";
                break;
            case R.id.btnPaySoon:
                startProblemPay();
                break;
            case R.id.textViewAddressSelect:
                reqestAddress();
                break;
            case R.id.bt_release:
                //todo 判断参数是否完整
                if (!getAllDataOk()) {
                    return;
                }
                showFirstPop();
                break;
            case R.id.layout_father:
                popupWindow.dismiss();
                break;
            case R.id.btnStudy:
                Intent intent =new Intent(this, GoToActivity.class);
                intent.putExtra(ConstantString.TITLE_NAME,"快速入门");
                intent.putExtra(ConstantString.URL,ConstantUrl.getInroduceUrl(customItemBean.getType()));
                startActivity(intent);
                break;
        }
    }

    private void showFirstPop() {
        if (priceBean != null) {
            PopCustomed popCustomed = new PopCustomed(getContext(), priceBean, customItemBean.getTitle(), customItemBean.getMark(), new PopCustomed.PayPopClickListenner() {
                @Override
                public void gotoMember() {
                    MyMemberActivity.start(getContext());
                }

                @Override
                public void gotoPay() {
                    if (popupWindow != null) {
                        popupWindow.showAsDropDown(contentView);
                    } else {
                        setPopwindow();
                    }
                }
            });
            popCustomed.show();
        }
    }

    private void startProblemPay() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());
        map.put("type", payType);
        map.put("money", priceBean.getO().getMoney() + "");
        CommonUtils.putDataIntoMap(map, "paymethod", paymethod);
        CommonUtils.putDataIntoMap(map, "user", user);
        CommonUtils.putDataIntoMap(map, "mobile", mobile);
        CommonUtils.putDataIntoMap(map, "date", dateString);
        CommonUtils.putDataIntoMap(map, "time", time);
        CommonUtils.putDataIntoMap(map, "address",textViewAddressSelect.getText().toString().trim() );
        post(ConstantUrl.customPayUrl, map, paymethod.equals("1") ? new WeixinPayModel() : new AliPayBean());
        btnPaySoon.setEnabled(false);
    }

    private void startWeixinPay(WeixinPayModel bean) {
        WeiChatInstallCheckUtils.checkWXIsInstalled();
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mContext, PlatformConfigConstant.WEIXIN_APP_ID);
        wxapi.registerApp(PlatformConfigConstant.WEIXIN_APP_ID);
        PayReq payReq = new PayReq();
        payReq.appId = PlatformConfigConstant.WEIXIN_APP_ID;
        payReq.partnerId = bean.getO().getPartnerid();
        payReq.prepayId = bean.getO().getPrepayid();
        payReq.packageValue = bean.getO().getPackageX();
        payReq.nonceStr = bean.getO().getNoncestr();
        payReq.timeStamp = bean.getO().getTimestamp() + "";
        payReq.sign = bean.getO().getSign();
        payReq.extData = "app data";
        wxapi.sendReq(payReq);
        if (payReq.prepayId.equals("")) {
            ToastUtil.showToast(mContext, "支付失败,请联系客服");
        }
        Log.e("prepayid", payReq.prepayId + "---");
    }

    private void startAlipay(AliPayBean bean) {
        new Alipay(this, this).startPay(bean.getO().toString());
    }


    @Override
    public void onSuccess(BaseBean data, int i) {
        super.onSuccess(data,i);
        if (data instanceof CustomInnerBean) {
            CustomInnerBean bean = (CustomInnerBean) data;
            webviewIntroduce.setText(bean.getO().getTips());
        }
        if (data instanceof PriceBean) {
            priceBean = (PriceBean) data;
        }
        if (data instanceof AliPayBean) {
            AliPayBean bean = (AliPayBean) data;
            startAlipay(bean);
            orderId = bean.getE();
        }
        if (data instanceof WeixinPayModel) {
            WeixinPayModel bean = (WeixinPayModel) data;
            startWeixinPay(bean);
            orderId = bean.getE();
        }
        if (data instanceof LawyerBean) {
            LawyerBean bean = (LawyerBean) data;
            LawyerBean.OBean listBean = bean.getO();
            String lsId = "ls" + listBean.getLawyer();
            Preference.put(ConstantString.OTHER_HEAD, listBean.getAvatar());
            Preference.put(ConstantString.OTHER_NAME, listBean.getName());
            Bundle bundle = new Bundle();
            bundle.putString(EaseConstant.PRO_ID, listBean.getId());
            bundle.putString(EaseConstant.TOCHAT_NAME, listBean.getName());
            bundle.putString(EaseConstant.PROBLEM, listBean.getTitle());
            bundle.putString(EaseConstant.TYPE, listBean.getType());
            if (HyEngine.isLogin()) {
                if(Preference.getBoolean(ConstantString.IS_GROUP)){
                    HyEngine.startConversationGroup(getActivity(),bean.getO().getGroup_id() , bundle, ChatActivity.class);
                }else {
                    HyEngine.startConversationSingle(getActivity(), lsId, bundle, ChatActivity.class);
                }//
            } else {
                HyEngine.login(getActivity(), Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING), true, lsId, bundle);
                Log.d("LOGCAT", "userId" + Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
            }
        }
        if(data instanceof AddressBean){
            AddressBean bean= (AddressBean) data;
            MyAddressInitTask execute = new MyAddressInitTask(this,bean.getO());
            //todo被测试要求不准设置默认地址
            execute.execute("", "", "");
            execute.setOnAddressPickListene(this);
        }
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
       super. onFailure(c_codeBean);
        setDialog(c_codeBean.getM());
    }
    private void setDialog(String message) {
        ArshowDialog.Builder builder = new ArshowDialog.Builder(mContext);
        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        builder.setPositiveButton("去完善", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                intent = new Intent(getContext(), MyprofileActivity.class);
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
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    public void setPopwindow() {
        //根据字数计算每个条目的长度
        contentView = View.inflate(getContext(), R.layout.pop_pay, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        TextView textViewPopHead = contentView.findViewById(R.id.textViewPopHead);
        textViewPopHead.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView textViewPreviusPrice = contentView.findViewById(R.id.textViewPreviusPrice);
        TextView textViewClosePop = contentView.findViewById(R.id.textViewPreviusPrice);
        textViewPreviusPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        TextView textViewRealPrice = contentView.findViewById(R.id.textViewRealPrice);
        RelativeLayout relative_weichat_pop = contentView.findViewById(R.id.relative_weichat_pop);
        RelativeLayout relative_alipay_pop = contentView.findViewById(R.id.relative_alipay_pop);
        ViewGroup layout_father = contentView.findViewById(R.id.layout_father);
        layout_father.setOnClickListener(this);
        textViewClosePop.setOnClickListener(this);
        imageViewWeichatSelect = contentView.findViewById(R.id.imageViewWeichatSelect);
        imageViewWeichatSelect.setImageResource(R.mipmap.select2x);
        imageViewAliSelect = contentView.findViewById(R.id.imageViewAliSelect);
        imageViewAliSelect.setImageResource(R.mipmap.no_selected2x);
        btnPaySoon = contentView.findViewById(R.id.btnPaySoon);
        relative_weichat_pop.setOnClickListener(this);
        relative_alipay_pop.setOnClickListener(this);
        btnPaySoon.setOnClickListener(this);
        textViewPreviusPrice.setText("¥" + priceBean.getO().getPrice() + "");
        textViewRealPrice.setText("¥" + priceBean.getO().getMoney() + "");
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnPaySoon.setEnabled(true);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onSuccess(String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());
        map.put("ordernum", orderId);
        post(ConstantUrl.lawyerUrl, map, new LawyerBean());
    }

    @Override
    public void onFailure(PayResult payResult) {
        btnPaySoon.setEnabled(true);
    }

    public boolean getAllDataOk() {
        long date = calenderview.getDate();
        dateString = TimeDataUtil.getTimeStamp(date, "yyyy-MM-dd");
        if (TextUtils.isEmpty(time)) {
            ToastUtil.toast("请选择上午还是下午");
            return false;
        }
        user = edittextContractName.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            ToastUtil.toast("请填写姓名");
            return false;
        }
        mobile = edittextContractPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.toast("请填写手机号码");
            return false;
        }
        if (!CommonUtils.isMobileNO(mobile)) {
            ToastUtil.toast("请填写正确的手机号码");
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == radioMorning.getId()) {
                time = "am";
            }
            if (buttonView.getId() == radioAfterloon.getId()) {
                time = "pm";
            }

        }
    }

    @Override
    public void setOnAddressPickListene(String province, String city, String count) {
        textViewAddressSelect.setText(city+"        "+count);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(btnPaySoon!=null){
            btnPaySoon.setEnabled(true);
        }
    }
}
