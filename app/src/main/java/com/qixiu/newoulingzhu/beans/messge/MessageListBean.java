package com.qixiu.newoulingzhu.beans.messge;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class MessageListBean extends BaseBean<MessageListBean.OMessageInfo> {


    /**
     * o : 1
     * e : {"list":[{"id":"1","problem":"我觉得真的恒号姮好手机卡还贷款就按户口","lawyer":"3","avatar":"","num":"0"},{"id":"4","problem":"尽快撒谎给大家卡号是可敬的哈空间大号卡号的科技爱好都是看","lawyer":"3","avatar":"","num":"0"}],"page":1}
     */


    private EMessageInfo e;

    public static class OMessageInfo {


        /**
         * xt : 1
         * xts : 1
         */

        private String xt;
        private int xts;

        public String getXt() {
            return xt;
        }

        public void setXt(String xt) {
            this.xt = xt;
        }

        public int getXts() {
            return xts;
        }

        public void setXts(int xts) {
            this.xts = xts;
        }
    }

    public EMessageInfo getE() {
        return e;
    }

    public void setE(EMessageInfo e) {
        this.e = e;
    }

    public static class EMessageInfo {
        /**
         * list : [{"id":"1","problem":"我觉得真的恒号姮好手机卡还贷款就按户口","lawyer":"3","avatar":"","num":"0"},{"id":"4","problem":"尽快撒谎给大家卡号是可敬的哈空间大号卡号的科技爱好都是看","lawyer":"3","avatar":"","num":"0"}]
         * page : 1
         */

        private int page;
        private List<ListBean> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * problem : 我觉得真的恒号姮好手机卡还贷款就按户口
             * lawyer : 3
             * avatar :
             * num : 0
             */

            private String id;
            private String problem;
            private String group_id;
            private String addtime;
            private String title;
            private String lawyer;
            private String avatar;
            private String num;
            private String name;
            private String type;
            private long lastMessageTime;

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public long getLastMessageTime() {
                return lastMessageTime;
            }

            public void setLastMessageTime(long lastMessageTime) {
                this.lastMessageTime = lastMessageTime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
