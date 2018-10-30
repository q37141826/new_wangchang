package com.wxpay;

import android.content.Context;

import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by 秀宝-段誉 on 2016-06-22 15:29.
 * <p/>
 * 说明：微信支付
 */
public class WxPay {

    public static int payOrderPosition = 0;

    private IWXAPI wxApi;

    public WxPay(Context context) {
        wxApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        wxApi.registerApp(PlatformConfigConstant.WEIXIN_APP_ID);

    }

    public void pay(WxPayInfo beans) {
        PayReq req = new PayReq();
        req.appId = beans.appid;
        req.partnerId = beans.partnerid;
        req.prepayId = beans.prepayid;
        req.nonceStr = beans.noncestr;
        req.timeStamp = beans.timestamp;
        req.packageValue = beans.packageValue;
        req.sign = beans.sign;
        req.extData = "app data"; // optional
        wxApi.sendReq(req);
    }

    /**
     * 判断用户手机上是否安装了微信客户端
     *
     * @return
     */
    public boolean isWXAppInstalledAndSupported() {

        boolean sIsWXAppInstalledAndSupported = wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }



}
