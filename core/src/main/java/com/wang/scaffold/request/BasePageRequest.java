package com.wang.scaffold.request;


import com.wang.scaffold.consts.WebConstants;

public class BasePageRequest {

    // 单页数据大小 默认10
    private int pageSize = WebConstants.DEFAULT_PAGE_SIZE;

    // 页码 默认1
    private int pageNum = WebConstants.DEFAULT_PAGE_NUM;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
