package com.qixiu.newoulingzhu.beans.home.consult;


import com.qixiu.qixiu.request.bean.BaseBean;

/**
 * Created by my on 2017/11/17.
 */

public class ProblemGotoPayBean extends BaseBean<String> {


    /**
     * pic : 0.02
     * zk : 0.02
     * productID : WanchangLawFirms06
     */

    private EBean e;

    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public static class EBean {
        private String pic;
        private String zk;
        private String productID;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getZk() {
            return zk;
        }

        public void setZk(String zk) {
            this.zk = zk;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }
    }
}
