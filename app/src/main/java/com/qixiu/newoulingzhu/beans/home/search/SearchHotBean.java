package com.qixiu.newoulingzhu.beans.home.search;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class SearchHotBean  extends BaseBean<List<SearchHotBean.SearchHotInfo>> {


    /**
     * o : [{"name":"欧诗漫"},{"name":"美宝莲"}]
     * e :
     */

    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }



    public static class SearchHotInfo {
        /**
         * name : 欧诗漫
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
