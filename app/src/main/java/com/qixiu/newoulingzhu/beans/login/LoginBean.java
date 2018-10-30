package com.qixiu.newoulingzhu.beans.login;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by HuiHeZe on 2017/7/21.
 */

public class LoginBean extends BaseBean<LoginBean.OBean> {


    /**
     * c : 1
     * m : 登陆成功
     * o : {"id":"28","phone":"18771112603","password":"123456","pid":"0","nickname":"187****2603","head":null,"regtime":null,"recentlogin":null,"clienttype":"1","deviceid":"8451213154563884","level":"2","integral":"1000","create_project":"0","buy_project":"0","is_verify":"0","promo_code":"10000","state":"1"}
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
        /**
         * id : 28
         * phone : 18771112603
         * password : 123456
         * pid : 0
         * nickname : 187****2603
         * head : null
         * regtime : null
         * recentlogin : null
         * clienttype : 1
         * deviceid : 8451213154563884
         * level : 2
         * integral : 1000
         * create_project : 0
         * buy_project : 0
         * is_verify : 0
         * promo_code : 10000
         * state : 1
         */
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String id;
        private String phone;
        private String password;
        private String pid;
        private String nickname;
        private String head;
        private Object regtime;
        private Object recentlogin;
        private String clienttype;
        private String deviceid;
        private String level;
        private String integral;
        private String create_project;
        private String buy_project;
        private String is_verify;
        private String promo_code;
        private String state;
        private String headimgurl;
        private String emlia;
        private String true_name;

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

        private String sex;

        public String getEmlia() {
            return emlia;
        }

        public void setEmlia(String emlia) {
            this.emlia = emlia;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public Object getRegtime() {
            return regtime;
        }

        public void setRegtime(Object regtime) {
            this.regtime = regtime;
        }

        public Object getRecentlogin() {
            return recentlogin;
        }

        public void setRecentlogin(Object recentlogin) {
            this.recentlogin = recentlogin;
        }

        public String getClienttype() {
            return clienttype;
        }

        public void setClienttype(String clienttype) {
            this.clienttype = clienttype;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getCreate_project() {
            return create_project;
        }

        public void setCreate_project(String create_project) {
            this.create_project = create_project;
        }

        public String getBuy_project() {
            return buy_project;
        }

        public void setBuy_project(String buy_project) {
            this.buy_project = buy_project;
        }

        public String getIs_verify() {
            return is_verify;
        }

        public void setIs_verify(String is_verify) {
            this.is_verify = is_verify;
        }

        public String getPromo_code() {
            return promo_code;
        }

        public void setPromo_code(String promo_code) {
            this.promo_code = promo_code;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
