package com.mx.ymate.excel.analysis.bean;


/**
 * @Author: mengxiang.
 * @Date: 2019-07-20.
 * @Time: 14:54.
 * @Description:
 */
public class CellResult {

    private Object value;

    private ErrorInfo errorInfo;

    public CellResult() {
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }
}
