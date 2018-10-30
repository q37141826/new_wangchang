package com.qixiu.newoulingzhu.beans.home.search;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class SearchAllClassifyBean extends BaseBean<List<SearchAllClassifyBean.SearchAllClassifyInfo>> {


    /**
     * o : [{"id":"31","name":"妈咪宝贝","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"28","name":"美容仪器","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"30","name":"日系美妆","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"29","name":"高端护肤","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"32","name":"生活数码","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"33","name":"男士专区","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"},{"id":"34","name":"食品保健","icon":"http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg","child":"美容仪器一/洁面仪一/美容针一"}]
     * e :
     */

    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }


    public static class SearchAllClassifyInfo {
        /**
         * id : 31
         * name : 妈咪宝贝
         * icon : http://www.cherry1.com/Upload/img/2015-06-20/558445021b2f3.jpg
         * child : 美容仪器一/洁面仪一/美容针一
         */

        private String id;
        private String name;
        private String icon;
        private String child;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }
    }
}
