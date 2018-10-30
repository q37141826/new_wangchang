package com.qixiu.newoulingzhu.beans.mine_bean;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by HuiHeZe on 2017/7/21.
 */

public class MyCenterBean extends BaseBean<MyCenterBean.OBean> {



    public static class OBean {


        /**
         * id : 1
         * num : -
         * stime :
         * etime :
         * true_name : 完美
         * head : /public/images/headicon.png
         * time : -
         * nickname : 去求
         * swit : 1
         */

        private String id;
        private String num;
        private String stime;
        private String etime;
        private String true_name;
        private String head;
        private String time;
        private String nickname;
        private String swit;
        private String count;
        private String level;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public String getEtime() {
            return etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSwit() {
            return swit;
        }

        public void setSwit(String swit) {
            this.swit = swit;
        }
    }
}

