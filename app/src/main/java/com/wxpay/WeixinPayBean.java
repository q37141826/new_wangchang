package com.wxpay;

import com.google.gson.annotations.SerializedName;
import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by HuiHeZe on 2017/6/28.
 */

public class WeixinPayBean extends BaseBean<WeixinPayBean.OBean> {


    /**
     * c : 1
     * m : 微信支付启动
     * o : {"appid":"wxe70844498f2fcb84","noncestr":"e34a5fc70ffc1475d4480fd85a3b31db","package":"Sign=WXPay","partnerid":"1487993832","prepayid":"wx20171106111740636ad64e310224087672","timestamp":1509938261,"sign":"E4F62B3CCC8894C0FF2633A234E7EF73"}
     * e :
     */



    public static class OBean {
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private int timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
