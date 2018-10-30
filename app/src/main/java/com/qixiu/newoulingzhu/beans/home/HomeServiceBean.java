package com.qixiu.newoulingzhu.beans.home;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2018/7/3.
 */

public class HomeServiceBean extends BaseBean<HomeServiceBean.OBean> {


    public static class OBean {
        private String id;
        private String user_login;
        private String user_nicename;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_login() {
            return user_login;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
