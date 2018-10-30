package com.alipay.interf;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2017/11/20.
 */

public class AliPayBean extends BaseBean<String> {


    /**
     * o : partner="2088821814283159"&seller_id="szwcgk@163.com"&out_trade_no="20171201173436"&subject="会员缴费"&body="100"&total_fee="100"&notify_url="http://train.whtkl.cn/PHPalipay/notify_url.php"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&trade_money="100"&sign=""&sign_type="RSA"
     * e :
     */

    private String e;



    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }
}
