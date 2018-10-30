package com.qixiu.newoulingzhu.beans.mine_bean.member;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class DredgeMemberPromptPayBean extends BaseBean<DredgeMemberPromptPayBean.DredgeMemberPromptPayInfo> {


    /**
     * o : {"id":"431","money":"600","order":"20171103178295"}
     * e :
     */

    private String e;



    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class DredgeMemberPromptPayInfo  {
        /**
         * id : 431
         * money : 600
         * order : 20171103178295
         */

        private String id;
        private String money;
        private String order;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}
