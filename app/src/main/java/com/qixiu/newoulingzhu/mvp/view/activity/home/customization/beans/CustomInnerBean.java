package com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

public  class CustomInnerBean extends BaseBean<CustomInnerBean.OBean> {

        public static class OBean {
            private String id;
            private String title;
            private String type;
            private String process;
            private String add_time;
            private String up_time;
            private String status;
            private String price;
            private String tips;
            private String mark;
            private String explain;
            private String logoimg;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getProcess() {
                return process;
            }

            public void setProcess(String process) {
                this.process = process;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getUp_time() {
                return up_time;
            }

            public void setUp_time(String up_time) {
                this.up_time = up_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTips() {
                return tips;
            }

            public void setTips(String tips) {
                this.tips = tips;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getExplain() {
                return explain;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getLogoimg() {
                return logoimg;
            }

            public void setLogoimg(String logoimg) {
                this.logoimg = logoimg;
            }
        }
    }