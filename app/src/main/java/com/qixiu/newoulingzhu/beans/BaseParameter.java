package com.qixiu.newoulingzhu.beans;


import com.qixiu.newoulingzhu.constant.IntConstant;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class BaseParameter {
    private int pageNo = 1;
    private int pageSize = IntConstant.RESOURCE_LIMIT;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}
