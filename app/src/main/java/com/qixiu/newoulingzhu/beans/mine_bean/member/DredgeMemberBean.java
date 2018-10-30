package com.qixiu.newoulingzhu.beans.mine_bean.member;

import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;


/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class DredgeMemberBean extends BaseBean<List<DredgeMemberBean.DredgeMemberInfo>> {


    /**
     * o : [{"id":"1","name":"体验会员"},{"id":"2","name":"白银会员"},{"id":"3","name":"黄金会员"},{"id":"4","name":"铂金会员"}]
     * e :
     */

    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }



    public static class DredgeMemberInfo {
        /**
         * id : 1
         * name : 体验会员
         */

        private String id;
        private String name;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

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
    }
}
