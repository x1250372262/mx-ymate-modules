package com.mx.ymate.dev.support.page;


import net.ymate.platform.core.persistence.IResultSet;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 分页基类
 */
public class Pages<T> implements Serializable {

    /**
     * 当前页码
     */
    private long pageNum;

    /**
     * 每页数量
     */
    private long pageSize;

    /**
     * 总页数
     */
    private long pageCount;

    /**
     * 总条数
     */
    private long recordCount;

    /**
     * 数据
     */
    private List<T> resultData;

    public static <T> Pages<T> create(IResultSet<T> resultSet){
        return new Pages<>(resultSet.getPageNumber(),resultSet.getPageSize(),resultSet.getPageCount(),resultSet.getRecordCount(),resultSet.getResultData());
    }

    public Pages() {
    }

    public Pages(long pageNum, long pageSize, long pageCount, long recordCount, List<T> resultData) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.recordCount = recordCount;
        this.resultData = resultData;
    }

    public Pages(long pageNum, long pageSize, long pageCount, long recordCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.recordCount = recordCount;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public List<T> getResultData() {
        return resultData;
    }

    public void setResultData(List<T> resultData) {
        this.resultData = resultData;
    }
}
