package com.qixiu.newoulingzhu.beans.home.cases;

import android.os.Parcel;
import android.os.Parcelable;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/8/23.
 */

public class CasesListBean extends BaseBean<CasesListBean.OBean>{

    public static class OBean {
        private String page;
        private int total_page;
        /**
         * id : 4
         * title : 呵呵哒
         * typeid : 11
         * up_time : 2018-08-23 15:56:42
         * push_time :
         * thumbnail : http://192.168.1.173/data/upload/https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535021068125&di=1caecf5c8569ac00d04655abca2f4aaa&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Fface%2Fb62
         */

        private List<DataBean> data;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
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
            private String title;
            private String typeid;
            private String up_time;
            private String push_time;
            private String thumbnail;
            private String  articlesmeta;
            private String  summary;
            private String  type;
            private boolean isList=false;
            private boolean isLastVisbel=false;

            public boolean isList() {
                return isList;
            }

            public void setList(boolean list) {
                isList = list;
            }

            public boolean isLastVisbel() {
                return isLastVisbel;
            }

            public void setLastVisbel(boolean lastVisbel) {
                isLastVisbel = lastVisbel;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getArticlesmeta() {
                return articlesmeta;
            }

            public void setArticlesmeta(String articlesmeta) {
                this.articlesmeta = articlesmeta;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTypeid() {
                return typeid;
            }

            public void setTypeid(String typeid) {
                this.typeid = typeid;
            }

            public String getUp_time() {
                return up_time;
            }

            public void setUp_time(String up_time) {
                this.up_time = up_time;
            }

            public String getPush_time() {
                return push_time;
            }

            public void setPush_time(String push_time) {
                this.push_time = push_time;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
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
                dest.writeString(this.title);
                dest.writeString(this.typeid);
                dest.writeString(this.up_time);
                dest.writeString(this.push_time);
                dest.writeString(this.thumbnail);
                dest.writeString(this.articlesmeta);
                dest.writeString(this.summary);
                dest.writeString(this.type);
                dest.writeByte(this.isList ? (byte) 1 : (byte) 0);
                dest.writeByte(this.isLastVisbel ? (byte) 1 : (byte) 0);
            }

            protected DataBean(Parcel in) {
                this.id = in.readString();
                this.title = in.readString();
                this.typeid = in.readString();
                this.up_time = in.readString();
                this.push_time = in.readString();
                this.thumbnail = in.readString();
                this.articlesmeta = in.readString();
                this.summary = in.readString();
                this.type = in.readString();
                this.isList = in.readByte() != 0;
                this.isLastVisbel = in.readByte() != 0;
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
