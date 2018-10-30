package com.qixiu.wigit.picker;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/9/6.
 */

public class AddressBean extends BaseBean<List<AddressBean.OBean>> {

    public static class OBean {
        private String id;
        private String name;
        /**
         * code : 7
         * name : 武汉市
         * arealist : [{"code":"15","name":"汉阳"},{"code":"17","name":"洪山"},{"code":"18","name":"汉口"}]
         */

        private List<CitylistBean> citylist;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
             * code : 15
             * name : 汉阳
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
                private String code;
                private String name;

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
            }
        }
    }
}
