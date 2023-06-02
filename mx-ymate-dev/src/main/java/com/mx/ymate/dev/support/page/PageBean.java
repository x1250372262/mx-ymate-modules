package com.mx.ymate.dev.support.page;


import net.ymate.platform.core.persistence.Page;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 分页bean
 */
public class PageBean {

    private Integer page;

    private Integer pageSize;

    public PageBean() {
    }

    public PageBean(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Page toPage(){
        return Page.createIfNeed(page,pageSize);
    }

    public static PageBean create(){
        return new PageBean(0,0);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
