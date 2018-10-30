package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/9/11
 */

public class RaiseResultBean extends BaseBean<RaiseResultBean.OBean> {


    public static class OBean {
        private double total;
        /**
         * money : 3875.4545454545
         * age : 66
         */

        private List<ListBean> list;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private double money;
            private String age;

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }
        }
    }
}
