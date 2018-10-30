package com.qixiu.wanchang.wxapi;

import com.google.gson.annotations.SerializedName;
import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by Administrator on 2016/12/26.
 */
public class WeixinPayModel extends BaseBean<WeixinPayModel.OBean> {


    /**
     * c : 1
     * m : 微信支付启动
     * o : {"appid":"wxc4c89680a32becf4","noncestr":"151627c95bfcf583124b06294fb4e253","package":"Sign=WXPay","partnerid":"1494497872","prepayid":"wx20171218140017e9d95a9ca40681372647","timestamp":1513576817,"sign":"35B21EB9F3B6DC03260385FF847BC6A9"}
     * e :
     */


    private String e;



    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

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
