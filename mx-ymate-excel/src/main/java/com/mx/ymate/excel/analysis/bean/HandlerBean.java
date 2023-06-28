package com.mx.ymate.excel.analysis.bean;

import com.mx.ymate.excel.analysis.IDefaultClass;
import com.mx.ymate.excel.analysis.annotation.ImportColumn;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author: mengxiang.
 * @Date: 2019-07-20.
 * @Time: 14:18.
 * @Description: 数据处理bean
 */
public class HandlerBean {

    private Object dataHandle;

    private Method method;

    private Class<?> parameterType;

    private HandlerBean(ImportColumn importColumn) throws Exception {
        Class<?> handleClass = importColumn.handleClass();
        if (!handleClass.isInterface() && StringUtils.isNotBlank(importColumn.method())) {
            dataHandle = handleClass.newInstance();
            //得到方法对象,有参的方法需要指定参数类型
            Class<?> parameterTypes = importColumn.parameterType();
            if (!parameterTypes.getName().equals(IDefaultClass.class.getName())) {
                method = dataHandle.getClass().getMethod(importColumn.method(), parameterTypes);
            } else {
                method = dataHandle.getClass().getMethod(importColumn.method());
            }
        }
    }

    public static HandlerBean create(ImportColumn importColumn) throws Exception {
        return new HandlerBean(importColumn);
    }

    public Object getDataHandle() {
        return dataHandle;
    }

    public void setDataHandle(Object dataHandle) {
        this.dataHandle = dataHandle;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }
}
