package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/9/11

 */

public class InjuryResultBean extends BaseBean<InjuryResultBean.OBean> {


    public static class OBean {
        private String total;
        private String docurl;
        private String docname;
        private String contents;

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        /**

         * deathburial : 27951.6
         * type : fixed
         * title : 丧葬补助金
         * isSelected : true
         * text : 27951.6
         */

        private List<ListBean> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDocurl() {
            return docurl;
        }

        public void setDocurl(String docurl) {
            this.docurl = docurl;
        }

        public String getDocname() {
            return docname;
        }

        public void setDocname(String docname) {
            this.docname = docname;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String type;
            private String title;
            private boolean isSelected;
            private String text;
            private String damages;
            private String allowance;
            private int position=0;
            private String price;
            private String singlePrice;
            private String deathburial;

            public String getDeathburial() {
                return deathburial;
            }

            public void setDeathburial(String deathburial) {
                this.deathburial = deathburial;
            }

            public String getDamages() {
                return damages;
            }

            public void setDamages(String damages) {
                this.damages = damages;
            }

            public String getAllowance() {
                return allowance;
            }

            public void setAllowance(String allowance) {
                this.allowance = allowance;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getSinglePrice() {
                return singlePrice;
            }

            public void setSinglePrice(String singlePrice) {
                this.singlePrice = singlePrice;
            }



            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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
        }
    }
}
