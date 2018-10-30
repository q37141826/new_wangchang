package com.qixiu.newoulingzhu.beans.member;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class MyMemberBean extends BaseBean<MyMemberBean.MyMemberInfo> {


    /**
     * o : {"id":"1","num":"-","stime":"","etime":"","true_name":"完美","head":"/public/images/headicon.png","level":"0","type":1,"vipname":"未开通","time":"-"}
     * e : {"gz":"1.号对接口健身卡就好收到货就安徽打卡机按户口 2.接口啥可敬的好看 3.的数量卡机宽带连接三","qy":"1.接口的耗时间看看 2贷款啦建档立卡骄傲了"}
     */


    private EBean e;


    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public static class MyMemberInfo {
        /**
         * id : 1
         * num : -
         * stime :
         * etime :
         * true_name : 完美
         * head : /public/images/headicon.png
         * level : 0
         * type : 1
         * vipname : 未开通
         * time : -
         */

        private String id;
        private String num;
        private String stime;
        private String etime;
        private String true_name;
        private String head;
        private String level;
        private String type;
        private String vipname;
        private String time;
        private String name;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVipname() {
            return vipname;
        }

        public void setVipname(String vipname) {
            this.vipname = vipname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class EBean {
        /**
         * gz : 1.号对接口健身卡就好收到货就安徽打卡机按户口 2.接口啥可敬的好看 3.的数量卡机宽带连接三
         * qy : 1.接口的耗时间看看 2贷款啦建档立卡骄傲了
         */

        private String gz;
        private String qy;

        public String getGz() {
            return gz;
        }

        public void setGz(String gz) {
            this.gz = gz;
        }

        public String getQy() {
            return qy;
        }

        public void setQy(String qy) {
            this.qy = qy;
        }
    }
}
