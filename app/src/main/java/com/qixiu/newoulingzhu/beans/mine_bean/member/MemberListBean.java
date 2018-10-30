package com.qixiu.newoulingzhu.beans.mine_bean.member;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/8/28.
 */

public class MemberListBean extends BaseBean<MemberListBean.OBean> {


    public static class OBean {
        /**
         * id : 2
         * name : 钻石会员
         * money : 288
         * num : 1
         * discount : 71
         * date : 7
         * addtime : 1531463120
         * download_num : 1
         */

        private List<BuyBean> buy;
        /**
         * id : 3
         * name : 星耀会员
         * money : 200
         * num : 2
         * discount : 71
         * date : 7
         * addtime : 1531707935
         * download_num : 3
         */

        private List<UpBean> up;

        public List<BuyBean> getBuy() {
            return buy;
        }

        public void setBuy(List<BuyBean> buy) {
            this.buy = buy;
        }

        public List<UpBean> getUp() {
            return up;
        }

        public void setUp(List<UpBean> up) {
            this.up = up;
        }

        public static class BuyBean implements BuyOrUpData {
            private String id;
            private String name;
            private String money;
            private String num;
            private String discount;
            private String date;
            private String addtime;
            private String download_num;
            private boolean is_selected = false;

            @Override
            public boolean isIs_selected() {
                return is_selected;
            }

            @Override
            public void setIs_selected(boolean is_selected) {
                this.is_selected = is_selected;
            }

            @Override
            public String getId() {
                return id;
            }
            @Override
            public void setId(String id) {
                this.id = id;
            }
            @Override
            public String getName() {
                return name;
            }
            @Override
            public void setName(String name) {
                this.name = name;
            }
            @Override
            public String getMoney() {
                return money;
            }
            @Override
            public void setMoney(String money) {
                this.money = money;
            }
            @Override
            public String getNum() {
                return num;
            }
            @Override
            public void setNum(String num) {
                this.num = num;
            }
            @Override
            public String getDiscount() {
                return discount;
            }
            @Override
            public void setDiscount(String discount) {
                this.discount = discount;
            }
            @Override
            public String getDate() {
                return date;
            }
            @Override
            public void setDate(String date) {
                this.date = date;
            }
            @Override
            public String getAddtime() {
                return addtime;
            }
            @Override
            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
            @Override
            public String getDownload_num() {
                return download_num;
            }
            @Override
            public void setDownload_num(String download_num) {
                this.download_num = download_num;
            }

        }

        public static class UpBean implements BuyOrUpData{
            private String id;
            private String name;
            private String money;
            private String num;
            private String discount;
            private String date;
            private String addtime;
            private String download_num;
            private boolean is_selected = false;

            @Override
            public boolean isIs_selected() {
                return is_selected;
            }

            @Override
            public void setIs_selected(boolean is_selected) {
                this.is_selected = is_selected;
            }
            @Override
            public String getId() {
                return id;
            }

            @Override
            public void setId(String id) {
                this.id = id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String getMoney() {
                return money;
            }

            @Override
            public void setMoney(String money) {
                this.money = money;
            }

            @Override
            public String getNum() {
                return num;
            }

            @Override
            public void setNum(String num) {
                this.num = num;
            }

            @Override
            public String getDiscount() {
                return discount;
            }

            @Override
            public void setDiscount(String discount) {
                this.discount = discount;
            }

            @Override
            public String getDate() {
                return date;
            }

            @Override
            public void setDate(String date) {
                this.date = date;
            }

            @Override
            public String getAddtime() {
                return addtime;
            }

            @Override
            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            @Override
            public String getDownload_num() {
                return download_num;
            }

            @Override
            public void setDownload_num(String download_num) {
                this.download_num = download_num;
            }
        }
    }

    public interface BuyOrUpData {

        boolean isIs_selected();
        void setIs_selected(boolean is_selected);
        String getId();
        void setId(String id);

        String getName();

        void setName(String name);

        String getMoney();

        void setMoney(String money);

        String getNum();

        void setNum(String num);

        String getDiscount();

        void setDiscount(String discount);

        String getDate();

        void setDate(String date);

        String getAddtime();

        void setAddtime(String addtime);

        String getDownload_num();

        void setDownload_num(String download_num);

    }

}
