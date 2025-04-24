package com.mx.ymate.dev.support.page;


import net.ymate.platform.core.persistence.IResultSet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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

    /**
     * 附件属性
     */
    private Map<String,Object> attrData;

    public static <T> Pages<T> create(IResultSet<T> resultSet) {
        return new Pages<>(resultSet.getPageNumber(), resultSet.getPageSize(), resultSet.getPageCount(), resultSet.getRecordCount(), resultSet.getResultData());
    }

    public static <T> Pages<T> create(List<T> list) {
        long size = list.size();
        return new Pages<>(1,size,1, size, list);
    }

    public Pages<T> attr(String key,Object value){
        attrData.put(key,value);
        return this;
    }

    public Object attr(String key){
        return attrData.get(key);
    }

    public Pages<T> attrs(Map<String,Object> attrs){
        attrData.putAll(attrs);
        return this;
    }

    public Pages<T> census(Map<String,Object> censusData){
        attrData.put("census",censusData);
        return this;
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

    public Map<String, Object> getAttrData() {
        return attrData;
    }

    public void setAttrData(Map<String, Object> attrData) {
        this.attrData = attrData;
    }
}
