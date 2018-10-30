package com.qixiu.newoulingzhu.beans.home.consult;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2018/6/21.
 */

public class ConsultingInroductionBean extends BaseBean<ConsultingInroductionBean.OBean> {



    public static class OBean {
        private String title;
        private String count;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
