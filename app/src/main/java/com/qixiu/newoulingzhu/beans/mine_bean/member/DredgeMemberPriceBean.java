package com.qixiu.newoulingzhu.beans.mine_bean.member;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class DredgeMemberPriceBean extends BaseBean<DredgeMemberPriceBean.DredgeMemberPriceInfo> {


    /**
     * o : {"money":"800","date":"一年"}
     * e :
     */


    private String e;



    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class DredgeMemberPriceInfo {
        /**
         * money : 800
         * date : 一年
         */

        private String money;
        private String date;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
