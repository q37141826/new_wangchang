package com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

public  class LawyerBean extends BaseBean<LawyerBean.OBean> {
        public static class OBean {
            private String id;
            private String problem;
            private String lawyer;
            private String type;
            private String addtime;
            private String prot_id;
            private String group_id;
            private String avatar;
            private String name;
            private int num;
            private String title;

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProblem() {
                return problem;
            }

            public void setProblem(String problem) {
                this.problem = problem;
            }

            public String getLawyer() {
                return lawyer;
            }

            public void setLawyer(String lawyer) {
                this.lawyer = lawyer;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getProt_id() {
                return prot_id;
            }

            public void setProt_id(String prot_id) {
                this.prot_id = prot_id;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }