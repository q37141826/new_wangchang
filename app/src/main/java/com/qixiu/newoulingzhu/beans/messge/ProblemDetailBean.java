package com.qixiu.newoulingzhu.beans.messge;


import android.os.Parcel;
import android.os.Parcelable;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class ProblemDetailBean extends BaseBean<ProblemDetailBean.ProblemDetailInfo> {


    /**
     * o : {"id":"1","uid":"1","problem":"我觉得真的恒号姮好手机卡还贷款就按户口","img":["http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png","http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png"],"addtime":"2017.11.02 10:20","type":"2","lawyer":"谭律师","ctime":"2017.11.02 13:40"}
     * e :
     */

    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class ProblemDetailInfo implements Parcelable {
        /**
         * id : 1
         * uid : 1
         * problem : 我觉得真的恒号姮好手机卡还贷款就按户口
         * img : ["http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png","http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png"]
         * addtime : 2017.11.02 10:20
         * type : 2
         * lawyer : 谭律师
         * ctime : 2017.11.02 13:40
         */
        private String id;
        private String uid;
        private String problem;
        private String addtime;
        private String group_id;

        private String address;
        private String connect_user;
        private String connect_moble;
        private String meet_date;
        private String meet_time;

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
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

        private String type;
        private String lawyer;
        private String ctime;
        private String avatar;
        private String lawyerid;
        private String qustiontype;
        private int speciality;
        private int attitude;
        private ArrayList<String> img;

        public String getLawyerid() {
            return lawyerid;
        }

        public void setLawyerid(String lawyerid) {
            this.lawyerid = lawyerid;
        }

        public String getQustiontype() {
            return qustiontype;
        }

        public void setQustiontype(String qustiontype) {
            this.qustiontype = qustiontype;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSpeciality() {
            return speciality;
        }

        public void setSpeciality(int speciality) {
            this.speciality = speciality;
        }

        public int getAttitude() {
            return attitude;
        }

        public void setAttitude(int attitude) {
            this.attitude = attitude;
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

        public ArrayList<String> getImg() {
            return img;
        }

        public void setImg(ArrayList<String> img) {
            this.img = img;
        }

        public ProblemDetailInfo() {
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
            dest.writeString(this.group_id);
            dest.writeString(this.address);
            dest.writeString(this.connect_user);
            dest.writeString(this.connect_moble);
            dest.writeString(this.meet_date);
            dest.writeString(this.meet_time);
            dest.writeString(this.type);
            dest.writeString(this.lawyer);
            dest.writeString(this.ctime);
            dest.writeString(this.avatar);
            dest.writeString(this.lawyerid);
            dest.writeString(this.qustiontype);
            dest.writeInt(this.speciality);
            dest.writeInt(this.attitude);
            dest.writeStringList(this.img);
        }

        protected ProblemDetailInfo(Parcel in) {
            this.id = in.readString();
            this.uid = in.readString();
            this.problem = in.readString();
            this.addtime = in.readString();
            this.group_id = in.readString();
            this.address = in.readString();
            this.connect_user = in.readString();
            this.connect_moble = in.readString();
            this.meet_date = in.readString();
            this.meet_time = in.readString();
            this.type = in.readString();
            this.lawyer = in.readString();
            this.ctime = in.readString();
            this.avatar = in.readString();
            this.lawyerid = in.readString();
            this.qustiontype = in.readString();
            this.speciality = in.readInt();
            this.attitude = in.readInt();
            this.img = in.createStringArrayList();
        }

        public static final Creator<ProblemDetailInfo> CREATOR = new Creator<ProblemDetailInfo>() {
            @Override
            public ProblemDetailInfo createFromParcel(Parcel source) {
                return new ProblemDetailInfo(source);
            }

            @Override
            public ProblemDetailInfo[] newArray(int size) {
                return new ProblemDetailInfo[size];
            }
        };
    }
}
