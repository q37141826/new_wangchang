package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator.beans;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

public class LawyerCaseBean extends BaseBean<List<LawyerCaseBean.OBean>> {

        public class OBean {
            private String type;
            private String title;
            private boolean is_asset;
            private boolean check;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean isIs_asset() {
                return is_asset;
            }

            public void setIs_asset(boolean is_asset) {
                this.is_asset = is_asset;
            }

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }
        }
    }