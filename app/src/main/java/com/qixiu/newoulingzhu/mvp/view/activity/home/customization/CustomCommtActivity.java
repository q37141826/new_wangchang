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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.qixiu.wanchang.R;
import com.qixiu.wanchang.wxapi.WeiChatInstallCheckUtils;
import com.qixiu.wanchang.wxapi.WeixinPayModel;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class CustomCommtActivity extends RequstActivity implements IPay {
    int images[] = {R.mipmap.custom_commt_hetong2x, R.mipmap.custom_commt_review2x, R.mipmap.custom_commt_lawyer_letter2x,
            R.mipmap.custom_commt_notification2x, R.mipmap.custom_commt_documents2x};
    String types[] = {"customized", "examination", "letterletter", "notice", "document"};
    //type ： 模块类型标识， letterletter 律师函 ， examination 合同审查，customized 合同定制，document 诉讼文书，meeting 会面咨询， notice 告知函；
    Map<String, Integer> imagesMap = new HashMap<>();
    int shortIntroduces[] = {R.string.custom_contract_short_introduce, R.string.custom_review_short_introduce,
            R.string.custom_lawyer_letter_short_introduce, R.string.custom_notification_short_introduce, R.string.custom_documents_short_introduce,};
    Map<String, Integer> shortIntroduceMap = new HashMap<>();


    @BindView(R.id.tomyright)
    ImageView tomyright;
    @BindView(R.id.btnStudy)
    Button btnStudy;
    @BindView(R.id.imageViewIntroduce)
    ImageView imageViewIntroduce;
    @BindView(R.id.textIntroduceHead)
    TextView textIntroduceHead;
    @BindView(R.id.textIntroduce)
    TextView textIntroduce;
    @BindView(R.id.et_answers_content)
    EditText etAnswersContent;
    @BindView(R.id.webviewIntroduce)
    TextView webviewIntroduce;
    @BindView(R.id.bt_release)
    Button btRelease;
    @BindView(R.id.relativeLayoutInroduce)
    RelativeLayout relativeLayoutInroduce;
    private CustomizationListActivity.CustomItemBean.OBean customItemBean;
    private PopupWindow popupWindow;
    private View contentView;
    private ImageView imageViewWeichatSelect;
    private ImageView imageViewAliSelect;
    private PriceBean priceBean;
    private String paymethod = "1";
    private String orderId;
    BroadcastReceiver receiver;

    public static void start(Context context, CustomizationListActivity.CustomItemBean.OBean bean) {
        Intent intent = new Intent(context, CustomCommtActivity.class);
        intent.putExtra(IntentDataKeyConstant.DATA, bean);
        context.startActivity(intent);
    }

    @Override
    public void adustTitle() {
//        super.adustTitle();
    }

    @Override
    protected void onInitData() {
        customItemBean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        mTitleView.setTitle(customItemBean.getTitle());
        for (int i = 0; i < types.length; i++) {
            imagesMap.put(types[i], images[i]);
            shortIntroduceMap.put(types[i], shortIntroduces[i]);
        }
        if (imagesMap.get(customItemBean.getType()) != null) {
            imageViewIntroduce.setImageResource(imagesMap.get(customItemBean.getType()));
            textIntroduce.setText(shortIntroduceMap.get(customItemBean.getType()));
        }
        textIntroduceHead.setText(customItemBean.getTitle());
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
        return R.layout.activity_custom_commt;
    }

    @Override
    protected void onInitViewNew() {
        btRelease.setOnClickListener(this);
        btnStudy.setOnClickListener(this);
        relativeLayoutInroduce.setOnClickListener(this);
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
            case R.id.bt_release:
                if (TextUtils.isEmpty(etAnswersContent.getText().toString().trim())) {
                    ToastUtil.toast("请简单描述您的服务需求");
                    return;
                }
                showFirstPop();
                break;
            case R.id.layout_father:
                popupWindow.dismiss();
                break;
            case R.id.btnStudy:
                Intent intent = new Intent(this, GoToActivity.class);
                intent.putExtra(ConstantString.TITLE_NAME, "快速入门");
                intent.putExtra(ConstantString.URL, ConstantUrl.getInroduceUrl(customItemBean.getType()));
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
        map.put("type", customItemBean.getType());
        map.put("money", priceBean.getO().getMoney() + "");
        CommonUtils.putDataIntoMap(map, "paymethod", paymethod);
        CommonUtils.putDataIntoMap(map, "problem", etAnswersContent.getText().toString());
        post(ConstantUrl.customPayUrl, map, paymethod.equals("1") ? new WeixinPayModel() : new AliPayBean());
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
            bundle.putString(EaseConstant.PROBLEM, listBean.getProblem());
            bundle.putString(EaseConstant.TYPE, listBean.getType());
            if (HyEngine.isLogin()) {
                if (Preference.getBoolean(ConstantString.IS_GROUP)) {
                    HyEngine.startConversationGroup(getActivity(), listBean.getGroup_id(), bundle, ChatActivity.class);
                } else {
                    HyEngine.startConversationSingle(getActivity(), lsId, bundle, ChatActivity.class);
                }
//
            } else {
                HyEngine.login(getActivity(), Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING), true, lsId, bundle);
                Log.d("LOGCAT", "userId" + Preference.get(IntentDataKeyConstant.ID, StringConstants.EMPTY_STRING));
            }
        }
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
    public void onError(Call call, Exception e, int i) {
        super.onError(call,e,i);
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        super.onFailure(c_codeBean);
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
        Button btnPaySoon = contentView.findViewById(R.id.btnPaySoon);
        relative_weichat_pop.setOnClickListener(this);
        relative_alipay_pop.setOnClickListener(this);
        btnPaySoon.setOnClickListener(this);
        textViewPreviusPrice.setText("¥" + priceBean.getO().getPrice() + "");
        textViewRealPrice.setText("¥" + priceBean.getO().getMoney() + "");
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


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
