package com.qixiu.newoulingzhu.mvp.view.activity.mine.pay;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.Alipay;
import com.alipay.PayResult;
import com.alipay.interf.AliPayBean;
import com.alipay.interf.IPay;
import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.utils.Preference;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.wanchang.R;
import com.qixiu.wanchang.wxapi.WeiChatInstallCheckUtils;
import com.qixiu.wanchang.wxapi.WeixinPayModel;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class AliWeixinPayActivity extends TitleActivity implements OKHttpUIUpdataListener, IPay {
    private OKHttpRequestModel okmdol;
    private Button btn_aliweixinPay;
    private RelativeLayout relative_layout_alipay, relative_layout_weixinpay;
    private ImageView imageView_circle_ali, imageView_circle_weixin;
    private int num;
    private String paymethod = 2 + "";
    private String type;
    private String order, id;
    private TextView textView_money_ailiweixinpay;
    private String money;

    @Override
    protected void onInitData() {
        type = getIntent().getStringExtra(IntentDataKeyConstant.TYPE);
        id = getIntent().getStringExtra(IntentDataKeyConstant.ID);
        order = getIntent().getStringExtra(IntentDataKeyConstant.ORDER_ID);
        num = getIntent().getIntExtra("num", 0);
        money = getIntent().getStringExtra(IntentDataKeyConstant.MONEY);
        textView_money_ailiweixinpay.setText(money);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_ali_weixin_pay;
    }

    @Override
    protected void onInitViewNew() {
        mTitleView.setTitle("支付");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        okmdol = new OKHttpRequestModel(this);
        btn_aliweixinPay = (Button) findViewById(R.id.btn_aliweixinPay);
        relative_layout_alipay = (RelativeLayout) findViewById(R.id.relative_layout_alipay);
        relative_layout_weixinpay = (RelativeLayout) findViewById(R.id.relative_layout_weixinpay);
        imageView_circle_ali = (ImageView) findViewById(R.id.imageView_circle_ali);
        imageView_circle_weixin = (ImageView) findViewById(R.id.imageView_circle_weixin);
        textView_money_ailiweixinpay = (TextView) findViewById(R.id.textView_money_ailiweixinpay);
        setClick();
        paymethod = "2";
        setState(imageView_circle_ali);
    }

    private void setClick() {
        btn_aliweixinPay.setOnClickListener(this);
        relative_layout_alipay.setOnClickListener(this);
        relative_layout_weixinpay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_aliweixinPay:
                if (TextUtils.isEmpty(order)) {
                    startProblemPay();
                    return;
                }
                startOrderPay();
                break;
            case R.id.relative_layout_alipay:
                paymethod = "2";
                setState(imageView_circle_ali);
                break;
            case R.id.relative_layout_weixinpay:
                paymethod = "1";
                setState(imageView_circle_weixin);
                break;
        }
    }

    private void startOrderPay() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("id", id + "");
        map.put("order", order + "");
        map.put("paymethod", paymethod + "");
        okmdol.okhHttpPost(ConstantUrl.startPayUrl, map, paymethod.equals("1") ? new WeixinPayModel() : new AliPayBean());

    }

    private void startProblemPay() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("num", num + "");
        map.put("money", money + "");
        map.put("paymethod", paymethod + "");
        okmdol.okhHttpPost(ConstantUrl.startProblemPayUrl, map, paymethod.equals("1") ? new WeixinPayModel() : new AliPayBean());
    }

    private void setState(ImageView imageView) {
        imageView_circle_ali.setImageResource(R.mipmap.selected_no2x);
        imageView_circle_weixin.setImageResource(R.mipmap.selected_no2x);
        imageView.setImageResource(R.mipmap.selected2x);
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof AliPayBean) {
            AliPayBean bean = (AliPayBean) data;
            startAlipay(bean);
        }
        if (data instanceof WeixinPayModel) {
            WeixinPayModel bean = (WeixinPayModel) data;
            startWeixinPay(bean);
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

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        ToastUtil.toast(c_codeBean.getM());
    }

    @Override
    public void onSuccess(String msg) {
        finish();
        try {
            AppManager.getAppManager().finishActivity(MyMemberActivity.class);
        } catch (Exception e) {
        }
    }

    @Override
    public void onFailure(PayResult payResult) {

    }
}
