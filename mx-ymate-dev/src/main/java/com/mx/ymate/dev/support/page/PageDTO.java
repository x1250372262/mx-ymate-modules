package com.mx.ymate.dev.support.page;


import com.mx.ymate.dev.util.BeanUtil;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 分页dto
 */
public class PageDTO implements Serializable {


    @RequestParam(defaultValue = "1")
    private Integer page;

    @RequestParam(defaultValue = "10")
    private Integer pageSize;

    public PageDTO() {
    }

    public PageBean toBean() throws Exception {
        return BeanUtil.copy(this, PageBean::new);
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

    @Override
    public String toString() {
        return "PageDTO{" + "page=" + page + ", pageSize=" + pageSize + '}';
    }
}
