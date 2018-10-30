package com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

public  class PriceBean extends BaseBean<PriceBean.OBean> {
        public static class OBean {
            private String uid;
            private String type;
            private String discount;
            private String price;
            private double money;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }
        }
    }