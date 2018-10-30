package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans;

import com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans.RaiseResultBean;
import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/9/10
 */

public class TrafficResultBean extends BaseBean<TrafficResultBean.OBean> {



    public static class OBean {
        private String docname;
        private String docurl;
        private String contents;

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        /**
         * type : fixed
         * isSelected : true
         * text :
         * damages : 1800000
         * title : 伤残赔偿金
         */

        private List<ListBean> list;

        public String getDocname() {
            return docname;
        }

        public void setDocname(String docname) {
            this.docname = docname;
        }

        public String getDocurl() {
            return docurl;
        }

        public void setDocurl(String docurl) {
            this.docurl = docurl;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String type;
            private boolean isSelected;
            private String text;
            private double damages;
            private String title;
            private String price;
            private String deathburial;
            private RaiseResultBean.OBean oBean;
            private boolean last;

            public boolean isLast() {
                return last;
            }

            public void setLast(boolean last) {
                this.last = last;
            }

            public String getDeathburial() {
                return deathburial;
            }

            public void setDeathburial(String deathburial) {
                this.deathburial = deathburial;
            }

            public RaiseResultBean.OBean getoBean() {
                return oBean;
            }

            public void setoBean(RaiseResultBean.OBean oBean) {
                this.oBean = oBean;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isIsSelected() {
                return isSelected;
            }

            public void setIsSelected(boolean isSelected) {
                this.isSelected = isSelected;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public double getDamages() {
                return damages;
            }

            public void setDamages(double damages) {
                this.damages = damages;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
