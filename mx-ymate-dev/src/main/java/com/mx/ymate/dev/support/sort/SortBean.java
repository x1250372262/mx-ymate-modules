package com.mx.ymate.dev.support.sort;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 排序bean
 */
public class SortBean {

    /**
     * 要排序的字段数组
     */
    private String[] fieldArray;

    /**
     * 和字段数组对应的排序方式
     */
    private String[] orderArray;

    /**
     * 是否执行排序
     */
    private boolean sort;

    /**
     * order是否匹配
     */
    private boolean order;

    public SortBean() {
    }

    public SortBean(String[] fieldArray, String[] orderArray) {
        this.fieldArray = fieldArray;
        this.orderArray = orderArray;
    }

    public SortBean(String[] fieldArray, String[] orderArray, boolean sort, boolean order) {
        this.fieldArray = fieldArray;
        this.orderArray = orderArray;
        this.sort = sort;
        this.order = order;
    }

    public SortBean(boolean sort) {
        this.sort = sort;
    }

    public String[] getFieldArray() {
        return fieldArray;
    }

    public void setFieldArray(String[] fieldArray) {
        this.fieldArray = fieldArray;
    }

    public String[] getOrderArray() {
        return orderArray;
    }

    public void setOrderArray(String[] orderArray) {
        this.orderArray = orderArray;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }
}
