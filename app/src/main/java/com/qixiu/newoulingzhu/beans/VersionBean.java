package com.qixiu.newoulingzhu.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by my on 2017/10/13.
 */
public class VersionBean {


    /**
     * c : 1
     * m : 查看成功
     * o : {"id":"1","iosname":"1.0.1","iostype":"1","name":"1.0.1","type":"1","content":"<p>更新了图片<br/><\/p><p><br/><\/p><p>版本1.1<\/p>","url":"http://www.cherry1.com/Upload/img/2017-09-11/59b5fde50231a.apk","addtime":"1506580043"}
     * e :
     */

    private int c;
    private String m;
    /**
     * id : 1
     * iosname : 1.0.1
     * iostype : 1
     * name : 1.0.1
     * type : 1
     * content : <p>更新了图片<br/></p><p><br/></p><p>版本1.1</p>
     * url : http://www.cherry1.com/Upload/img/2017-09-11/59b5fde50231a.apk
     * addtime : 1506580043
     */

    private OBean o;
    private String e;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public OBean getO() {
        return o;
    }

    public void setO(OBean o) {
        this.o = o;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class OBean implements Parcelable {
        private String id;
        private String iosname;
        private String iostype;
        private String name;
        private String type;
        private String content;
        private String url;
        private String addtime;
        private String appName;
        private String savePath;

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIosname() {
            return iosname;
        }

        public void setIosname(String iosname) {
            this.iosname = iosname;
        }

        public String getIostype() {
            return iostype;
        }

        public void setIostype(String iostype) {
            this.iostype = iostype;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.iosname);
            dest.writeString(this.iostype);
            dest.writeString(this.name);
            dest.writeString(this.type);
            dest.writeString(this.content);
            dest.writeString(this.url);
            dest.writeString(this.addtime);
            dest.writeString(this.appName);
            dest.writeString(this.savePath);
        }

        public OBean() {
        }

        protected OBean(Parcel in) {
            this.id = in.readString();
            this.iosname = in.readString();
            this.iostype = in.readString();
            this.name = in.readString();
            this.type = in.readString();
            this.content = in.readString();
            this.url = in.readString();
            this.addtime = in.readString();
            this.appName = in.readString();
            this.savePath = in.readString();
        }

        public static final Creator<OBean> CREATOR = new Creator<OBean>() {
            @Override
            public OBean createFromParcel(Parcel source) {
                return new OBean(source);
            }

            @Override
            public OBean[] newArray(int size) {
                return new OBean[size];
            }
        };
    }
}
