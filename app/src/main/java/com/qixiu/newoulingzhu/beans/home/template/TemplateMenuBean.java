package com.qixiu.newoulingzhu.beans.home.template;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by my on 2018/8/23.
 */

public class TemplateMenuBean extends BaseBean<List<TemplateMenuBean.OBean>> {

    public static class OBean {
        private String id;
        private String value;
        private boolean is_selected = false;

        public boolean isIs_selected() {
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
