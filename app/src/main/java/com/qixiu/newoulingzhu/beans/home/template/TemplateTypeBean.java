package com.qixiu.newoulingzhu.beans.home.template;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class TemplateTypeBean extends BaseBean<List<TemplateTypeBean.TemplateTypeInfo>> {


    /**
     * o : [{"id":"1","value":"婚姻"},{"id":"2","value":"房屋"},{"id":"3","value":"中介"}]
     * e :
     */

    private String e;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }



    public static class TemplateTypeInfo {
        /**
         * id : 1
         * value : 婚姻
         */

        private String id;
        private String value;
        private boolean is_selected=false;

        public boolean is_selected() {
            return is_selected;
        }

        public void setIs_selected(boolean is_selected) {
            this.is_selected = is_selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
