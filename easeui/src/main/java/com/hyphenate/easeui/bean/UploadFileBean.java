package com.hyphenate.easeui.bean;

import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2018/10/12.
 */

public class UploadFileBean extends BaseBean<UploadFileBean.OBean>{



    public static class OBean {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
