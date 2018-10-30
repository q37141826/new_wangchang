package com.qixiu.newoulingzhu.beans.home.consult;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by Administrator on 2017/11/21 0021.
 */

public class ConsultBean extends BaseBean<ConsultBean.OBean> {


    /**
     * e : {"avatar":"","id":"11","user_nicename":""}
     * o : 28
     */

    public class OBean {

        /**
         * id : 598
         * group_id : 61387251449857
         */

        private String id;
        private String group_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }
    }


    private ConsultInfo e;


    public ConsultInfo getE() {
        return e;
    }

    public void setE(ConsultInfo e) {
        this.e = e;
    }


    public static class ConsultInfo {
        /**
         * avatar :
         * id : 11
         * user_nicename :
         */

        private String avatar;
        private String id;
        private String user_nicename;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }
    }
}
