package com.qixiu.newoulingzhu.mvp.view.activity.home.caculator.result.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class HeadBean implements Parcelable {
        private String address;
        private String addressId;
        private String level;
        private String levelID;
        private String age;
        private String regist;
        private String registId;

    public HeadBean(String address, String addressId, String level, String levelID, String age, String regist, String registId) {
        this.address = address;
        this.addressId = addressId;
        this.level = level;
        this.levelID = levelID;
        this.age = age;
        this.regist = regist;
        this.registId = registId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
    }

    public String getRegistId() {
        return registId;
    }

    public void setRegistId(String registId) {
        this.registId = registId;
    }

    public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getRegist() {
            return regist;
        }

        public void setRegist(String regist) {
            this.regist = regist;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.addressId);
        dest.writeString(this.level);
        dest.writeString(this.levelID);
        dest.writeString(this.age);
        dest.writeString(this.regist);
        dest.writeString(this.registId);
    }

    public HeadBean() {
    }

    protected HeadBean(Parcel in) {
        this.address = in.readString();
        this.addressId = in.readString();
        this.level = in.readString();
        this.levelID = in.readString();
        this.age = in.readString();
        this.regist = in.readString();
        this.registId = in.readString();
    }

    public static final Creator<HeadBean> CREATOR = new Creator<HeadBean>() {
        @Override
        public HeadBean createFromParcel(Parcel source) {
            return new HeadBean(source);
        }

        @Override
        public HeadBean[] newArray(int size) {
            return new HeadBean[size];
        }
    };
}