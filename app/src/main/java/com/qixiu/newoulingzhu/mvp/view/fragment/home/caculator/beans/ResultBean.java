package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

public class ResultBean extends BaseBean<List<ResultBean.OBean>> {


    /**
     * doc_name :
     * contents :
     */

    private EBean e;

    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public static class EBean {
        private String doc_name;
        private String contents;

        public String getDoc_name() {
            return doc_name;
        }

        public void setDoc_name(String doc_name) {
            this.doc_name = doc_name;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }
    }

    public class OBean {
            private String title;
            private String money;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }
        }
    }