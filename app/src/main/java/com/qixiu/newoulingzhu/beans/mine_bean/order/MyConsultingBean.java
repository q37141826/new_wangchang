package com.qixiu.newoulingzhu.beans.mine_bean.order;


import android.os.Parcel;
import android.os.Parcelable;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2017/11/17.
 */

public class MyConsultingBean   extends BaseBean<MyConsultingBean.OBean> {



    public static class OBean {
        private int page;
        private int total_page;
        /**
         * id : 973
         * uid : 549
         * problem :
         * img : ["http://sk.qixiuu.com/data/upload/"]
         * addtime : 2018-11-05
         * type : 1
         * lawyer : 90
         * ctime :
         * status : 1
         * prot_id : 8
         * order_num : 20181105502410
         * meet_date : 2018-11-05
         * meet_time : am
         * address : 深圳  南山
         * connect_user : 小宝还得等会
         * connect_moble : 13554540382
         * group_id : 64993425948673
         * title : 会面咨询
         * money : 0.08
         * num : 1
         * avatar : http://sk.qixiuu.com/data/upload/admin/20181017/5bc68bd27bb58.png
         * user_nicename : orange3
         */

        private List<DataBean> data;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Parcelable {
            private String id;
            private String uid;
            private String problem;
            private String addtime;
            private String type;
            private String lawyer;
            private String ctime;
            private String status;
            private String prot_id;
            private String order_num;
            private String meet_date;
            private String meet_time;
            private String address;
            private String connect_user;
            private String connect_moble;
            private String group_id;
            private String title;
            private String money;
            private String num;
            private String avatar;
            private String user_nicename;
            private ArrayList<String> img;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getProblem() {
                return problem;
            }

            public void setProblem(String problem) {
                this.problem = problem;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLawyer() {
                return lawyer;
            }

            public void setLawyer(String lawyer) {
                this.lawyer = lawyer;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getProt_id() {
                return prot_id;
            }

            public void setProt_id(String prot_id) {
                this.prot_id = prot_id;
            }

            public String getOrder_num() {
                return order_num;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public String getMeet_date() {
                return meet_date;
            }

            public void setMeet_date(String meet_date) {
                this.meet_date = meet_date;
            }

            public String getMeet_time() {
                return meet_time;
            }

            public void setMeet_time(String meet_time) {
                this.meet_time = meet_time;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getConnect_user() {
                return connect_user;
            }

            public void setConnect_user(String connect_user) {
                this.connect_user = connect_user;
            }

            public String getConnect_moble() {
                return connect_moble;
            }

            public void setConnect_moble(String connect_moble) {
                this.connect_moble = connect_moble;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

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

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

            public ArrayList<String> getImg() {
                return img;
            }

            public void setImg(ArrayList<String> img) {
                this.img = img;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.uid);
                dest.writeString(this.problem);
                dest.writeString(this.addtime);
                dest.writeString(this.type);
                dest.writeString(this.lawyer);
                dest.writeString(this.ctime);
                dest.writeString(this.status);
                dest.writeString(this.prot_id);
                dest.writeString(this.order_num);
                dest.writeString(this.meet_date);
                dest.writeString(this.meet_time);
                dest.writeString(this.address);
                dest.writeString(this.connect_user);
                dest.writeString(this.connect_moble);
                dest.writeString(this.group_id);
                dest.writeString(this.title);
                dest.writeString(this.money);
                dest.writeString(this.num);
                dest.writeString(this.avatar);
                dest.writeString(this.user_nicename);
                dest.writeStringList(this.img);
            }

            public DataBean() {
            }

            protected DataBean(Parcel in) {
                this.id = in.readString();
                this.uid = in.readString();
                this.problem = in.readString();
                this.addtime = in.readString();
                this.type = in.readString();
                this.lawyer = in.readString();
                this.ctime = in.readString();
                this.status = in.readString();
                this.prot_id = in.readString();
                this.order_num = in.readString();
                this.meet_date = in.readString();
                this.meet_time = in.readString();
                this.address = in.readString();
                this.connect_user = in.readString();
                this.connect_moble = in.readString();
                this.group_id = in.readString();
                this.title = in.readString();
                this.money = in.readString();
                this.num = in.readString();
                this.avatar = in.readString();
                this.user_nicename = in.readString();
                this.img = in.createStringArrayList();
            }

            public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
                @Override
                public DataBean createFromParcel(Parcel source) {
                    return new DataBean(source);
                }

                @Override
                public DataBean[] newArray(int size) {
                    return new DataBean[size];
                }
            };
        }
    }
}
