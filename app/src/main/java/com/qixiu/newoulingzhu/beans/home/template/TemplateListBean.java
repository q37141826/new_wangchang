package com.qixiu.newoulingzhu.beans.home.template;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class TemplateListBean extends BaseBean<TemplateListBean.TemplateListInfo> {

    /**
     * o : {"list":[{"id":"8","title":"文书2","smeta":"http://www.wcls.com/data/upload/admin/20171101/59f980f122964.jpg"},{"id":"7","title":"文书1","smeta":"http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png"}],"page":1}
     * e :
     */


    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class TemplateListInfo {
        /**
         * list : [{"id":"8","title":"文书2","smeta":"http://www.wcls.com/data/upload/admin/20171101/59f980f122964.jpg"},{"id":"7","title":"文书1","smeta":"http://www.wcls.com/data/upload/admin/20171101/59f980a75d32b.png"}]
         * page : 1
         */

        private int page;
        private List<ListBean> list;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 8
             * title : 文书2
             * smeta : http://www.wcls.com/data/upload/admin/20171101/59f980f122964.jpg
             */

            private String id;
            private String title;
            private String smeta;
            private String read;
            private String count;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getRead() {
                return read;
            }

            public void setRead(String read) {
                this.read = read;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSmeta() {
                return smeta;
            }

            public void setSmeta(String smeta) {
                this.smeta = smeta;
            }
        }
    }
}
