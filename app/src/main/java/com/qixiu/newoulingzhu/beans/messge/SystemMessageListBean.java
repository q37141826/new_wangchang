package com.qixiu.newoulingzhu.beans.messge;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class SystemMessageListBean extends BaseBean<SystemMessageListBean.SystemMessageListInfo> {


    /**
     * o : {"list":[{"addtime":"2017-11-06 14:09","lawyer":"3","title":"您的咨询已经在处理中","mage":"您的咨询已经被谭小花律师进行处理啦，请及时查看。"}],"page":1}
     * e :
     */


    private String e;



    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class SystemMessageListInfo {
        /**
         * list : [{"addtime":"2017-11-06 14:09","lawyer":"3","title":"您的咨询已经在处理中","mage":"您的咨询已经被谭小花律师进行处理啦，请及时查看。"}]
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
             * addtime : 2017-11-06 14:09
             * lawyer : 3
             * title : 您的咨询已经在处理中
             * mage : 您的咨询已经被谭小花律师进行处理啦，请及时查看。
             */

            private String addtime;
            private String lawyer;
            private String title;
            private String mage;

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getLawyer() {
                return lawyer;
            }

            public void setLawyer(String lawyer) {
                this.lawyer = lawyer;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMage() {
                return mage;
            }

            public void setMage(String mage) {
                this.mage = mage;
            }
        }
    }
}


