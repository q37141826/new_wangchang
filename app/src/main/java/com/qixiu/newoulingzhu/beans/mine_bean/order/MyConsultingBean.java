package com.qixiu.newoulingzhu.beans.mine_bean.order;


import android.os.Parcel;
import android.os.Parcelable;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2017/11/17.
 */

public class MyConsultingBean  extends BaseBean<MyConsultingBean.OBean> {



    public static class OBean {
        private int page;
        private int total_page;
        /**
         * id : 171
         * uid : 421
         * problem : 小号测试一下
         * img : ["http://sk.qixiuu.com/data/upload/"]
         * addtime : 2018-08-29
         * type : 1
         * lawyer : 24
         * ctime :
         * status : 1
         * prot_id :
         * order_num :
         * title : 问题咨询
         * money : 0
         * num : 1
         * avatar : http://sk.qixiuu.com/data/upload/admin/20180411/5acdc6f859f3e.png
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
            private String title;
            private int money;
            private int num;
            private String avatar;
            private String user_nicename;
            private ArrayList<String> img;
            private String group_id;

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getUser_nicename() {
                return user_nicename;
            }

            public void setUser_nicename(String user_nicename) {
                this.user_nicename = user_nicename;
            }

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public ArrayList<String> getImg() {
                return img;
            }

            public void setImg(ArrayList<String> img) {
                this.img = img;
            }

            public DataBean() {
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
                dest.writeString(this.title);
                dest.writeInt(this.money);
                dest.writeInt(this.num);
                dest.writeString(this.avatar);
                dest.writeString(this.user_nicename);
                dest.writeStringList(this.img);
                dest.writeString(this.group_id);
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
                this.title = in.readString();
                this.money = in.readInt();
                this.num = in.readInt();
                this.avatar = in.readString();
                this.user_nicename = in.readString();
                this.img = in.createStringArrayList();
                this.group_id = in.readString();
            }

            public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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
