package com.qixiu.newoulingzhu.beans;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by HuiHeZe on 2017/7/21.
 */

public class SendCodeBean extends BaseBean<SendCodeBean.OBean> {


    public static class OBean {
        /**
         * verify_id : 53
         */
        private String verify_id;

        public String getVerify_id() {
            return verify_id;
        }

        public void setVerify_id(String verify_id) {
            this.verify_id = verify_id;
        }
    }
}
