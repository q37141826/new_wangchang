package com.qixiu.newoulingzhu.beans.mine_bean;

import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2018/8/30.
 */

public class MyProfileBean extends BaseBean<MyProfileBean.OBean>{


    public static class OBean {
        private String id;
        private String head;
        private String nickname;
        private String true_name;
        private String sex;
        private String address;
        private String emlia;
        private String phone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmlia() {
            return emlia;
        }

        public void setEmlia(String emlia) {
            this.emlia = emlia;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
