package com.qixiu.newoulingzhu.beans.home;


import com.qixiu.qixiu.request.bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class HomeBean extends BaseBean<HomeBean.HomeInfo> {


    /**
     * o : {"banner":[{"ad_id":"2","ad_content":"www.baidu.com","img":"http://www.wcls.com/data/upload/admin/20171101/59f968d57b06d.png"},{"ad_id":"1","ad_content":"www.baidu.com","img":"http://www.wcls.com/data/upload/admin/20171101/59f968d57b06d.png"}],"post":[{"id":"8","title":"实时响应"},{"id":"7","title":"会员制"},{"id":"6","title":"专业律师"},{"id":"5","title":"成熟律师"}]}
     * e :
     */


    private String e;


    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class HomeInfo {
        private List<BannerBean> banner;
        private List<PostBean> post;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public List<PostBean> getPost() {
            return post;
        }

        public void setPost(List<PostBean> post) {
            this.post = post;
        }

        public static class BannerBean {
            /**
             * ad_id : 2
             * ad_content : www.baidu.com
             * img : http://www.wcls.com/data/upload/admin/20171101/59f968d57b06d.png
             */

            private String ad_id;
            private String ad_content;
            private String img;

            public String getAd_id() {
                return ad_id;
            }

            public void setAd_id(String ad_id) {
                this.ad_id = ad_id;
            }

            public String getAd_content() {
                return ad_content;
            }

            public void setAd_content(String ad_content) {
                this.ad_content = ad_content;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public static class PostBean {
            /**
             * id : 8
             * title : 实时响应
             */

            private String id;
            private String title;
            private String count;
            private String addtime;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
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
        }
    }
}
