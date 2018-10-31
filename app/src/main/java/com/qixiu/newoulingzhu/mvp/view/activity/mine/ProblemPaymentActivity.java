package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.Alipay;
import com.alipay.PayResult;
import com.alipay.interf.AliPayBean;
import com.alipay.interf.IPay;
import com.qixiu.newoulingzhu.beans.home.consult.ConsultingInroductionBean;
import com.qixiu.newoulingzhu.beans.home.consult.ProblemGotoPayBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.member.MyMemberActivity;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowDialog;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

import static com.qixiu.newoulingzhu.constant.ConstantUrl.countMessageUrl;
import static com.qixiu.newoulingzhu.constant.ConstantUrl.is_info_enough;


public class ProblemPaymentActivity extends TitleActivity implements OKHttpUIUpdataListener, IPay {
    @BindView(R.id.imageViewCloseAd)
    ImageView imageViewCloseAd;
    private TextView textView_previous_price, textView_totoalPrice, textView_times_rest, btn_menber_rights;
    private ImageView imageView_add_problem_payment, imageView_minus_problem_payment;
    private TextView textView_pay_howlong_payment, textView_unitPrice;
    private int unitPrice = 0, unitPreviousPrice = 0;
    private int year = 1;
    private OKHttpRequestModel okmdol;
    private int times = 1;
    private Button btn_gotoPay;
    WebView webviewInroduce;
    private PopupWindow popupWindow;
    private ProblemGotoPayBean bean;
    private ImageView imageViewWeichatSelect;
    private ImageView imageViewAliSelect;
    private String paymethod = 1 + "";//1 微信pay  2 支付宝
    private ImageView imageViewAd;
    private View contentView;

    public static void start(Context context) {
        context.startActivity(new Intent(context, ProblemPaymentActivity.class));
    }

    @Override
    protected void onInitData() {
        okmdol = new OKHttpRequestModel(this);
        getBuyData();//购买的价钱折扣等数据
        getIntroduce();//说明部分
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_problem_payment;
    }

    @Override
    protected void onInitViewNew() {
        btn_gotoPay = (Button) findViewById(R.id.btn_gotoPay);
        imageViewAd = (ImageView) findViewById(R.id.imageViewAd);
        webviewInroduce = (WebView) findViewById(R.id.webviewInroduce);
        textView_totoalPrice = (TextView) findViewById(R.id.textView_totoalPrice);
        btn_menber_rights = (TextView) findViewById(R.id.btn_menber_rights);
        textView_previous_price = (TextView) findViewById(R.id.textView_previous_price);
        textView_previous_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        imageView_add_problem_payment = (ImageView) findViewById(R.id.imageView_add_problem_payment);
        imageView_add_problem_payment.setOnClickListener(this);
        imageView_minus_problem_payment = (ImageView) findViewById(R.id.imageView_minus_problem_payment);
        imageView_minus_problem_payment.setOnClickListener(this);
        imageViewAd.setOnClickListener(this);
        textView_pay_howlong_payment = (TextView) findViewById(R.id.textView_pay_howlong_payment);
        textView_unitPrice = (TextView) findViewById(R.id.textView_unitPrice);
        textView_times_rest = (TextView) findViewById(R.id.textView_times_rest);
        btn_gotoPay.setOnClickListener(this);
        mTitleView.setTitle("问题购买");
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_menber_rights.setOnClickListener(this);
        imageViewCloseAd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageView_add_problem_payment:
                year++;
                setState();
                break;
            case R.id.imageView_minus_problem_payment:
                year--;
                if (year == 0) {
                    year++;
                }
                setState();
                break;
            case R.id.btn_gotoPay:
                //这个地方弹出弹框
                if(popupWindow!=null){
                    popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
                }else {
                    setPopwindow();
                }
//                intent = new Intent(this, AliWeixinPayActivity.class);
//                intent.putExtra("num", year);
//                intent.putExtra(IntentDataKeyConstant.MONEY, unitPrice * year + "");
//                startActivity(intent);
                break;
            case R.id.btn_menber_rights:
            case R.id.imageViewAd:
                intent = new Intent(this, MyMemberActivity.class);
                startActivity(intent);
                break;
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
            case R.id.imageViewCloseAd:
                imageViewAd.setVisibility(View.GONE);
                break;
            case R.id.layout_father:
            case R.id.textViewClosePop:
                popupWindow.dismiss();
                break;
        }
    }

    private void startProblemPay() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        map.put("num", 1 + "");
        map.put("money", bean.getE().getZk() + "");
        map.put("paymethod", paymethod + "");
        okmdol.okhHttpPost(ConstantUrl.startProblemPayUrl, map, paymethod.equals("1") ? new WeixinPayModel() : new AliPayBean());
    }

    private void setState() {
        textView_pay_howlong_payment.setText(year + "");
        textView_totoalPrice.setText("¥" + year * unitPrice + "");
        textView_previous_price.setText("¥" + year * unitPreviousPrice + "");
        textView_times_rest.setText(times + "");
        textView_unitPrice.setText(unitPrice + "元");
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof ProblemGotoPayBean) {
            bean = (ProblemGotoPayBean) data;
            times = (int) Double.parseDouble(bean.getO().toString());
            textView_times_rest.setText(times + "");
            unitPrice = (int) Double.parseDouble(bean.getE().getZk());
            unitPreviousPrice = (int) Double.parseDouble(bean.getE().getPic());
            setState();
        }
        if (data instanceof ConsultingInroductionBean) {
            ConsultingInroductionBean bean = (ConsultingInroductionBean) data;
//            CommonUtils.setWebview(webviewInroduce, bean.getO().getCount(), true);

            WebSettings settings = webviewInroduce.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDefaultFontSize(10);
            webviewInroduce.loadDataWithBaseURL(null, bean.getO().getCount(), "text/html", "utf-8", null);


        }
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
        if (c_codeBean.getUrl().equals(is_info_enough) || c_codeBean.getUrl().equals(ConstantUrl.startProblemPayUrl)) {
            setDialog(c_codeBean.getM());
        }
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
    protected void onStart() {
        super.onStart();
        getBuyData();
    }

    public void getBuyData() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", Preference.get(ConstantString.USERID, ""));
        ProblemGotoPayBean bean = new ProblemGotoPayBean();
        okmdol.okhHttpPost(ConstantUrl.problemBuyUrl, map, bean);
    }

    public void getIntroduce() {
        Map<String, String> map = new HashMap<>();
        map.put("id", 10 + "");
        okmdol.okhHttpPost(countMessageUrl, map, new ConsultingInroductionBean());
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
        textViewPreviusPrice.setText("¥" + bean.getE().getPic() + "");
        textViewRealPrice.setText("¥" + bean.getE().getZk() + "");
    }

    @Override
    public void onSuccess(String msg) {
        popupWindow.dismiss();
    }

    @Override
    public void onFailure(PayResult payResult) {
        popupWindow.dismiss();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
