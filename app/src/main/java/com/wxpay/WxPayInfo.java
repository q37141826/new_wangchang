package com.wxpay;


import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class WxPayInfo {

    public String appid = PlatformConfigConstant.WEIXIN_APP_ID;    //应用APPID	string
    public String extend;    //扩展字段	string
    public String noncestr;    //随机字符串	string
    public String partnerid;    //商户号	string
    public String prepayid;    //预支付交易会话ID	string
    public String sign;    //签名	string
    public String timestamp;    //时间戳
    public String packageValue = "Sign=WXPay";

}
