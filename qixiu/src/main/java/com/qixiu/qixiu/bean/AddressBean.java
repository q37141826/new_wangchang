package com.qixiu.qixiu.bean;

import java.util.List;

/**
 * Created by my on 2018/9/3.
 */

public class AddressBean {


    private String code;
    private String name;

    private List<CitylistBean> citylist;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CitylistBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CitylistBean> citylist) {
        this.citylist = citylist;
    }

    public static class CitylistBean {
        private String code;
        private String name;
        /**
         * name : 东城区
         * code : 110101
         */

        private List<ArealistBean> arealist;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ArealistBean> getArealist() {
            return arealist;
        }

        public void setArealist(List<ArealistBean> arealist) {
            this.arealist = arealist;
        }

        public static class ArealistBean {
            private String name;
            private String code;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}
