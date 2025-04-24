package com.mx.ymate.excel.export.converter;

import com.mx.ymate.excel.export.annotation.MConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class HandlerBean {

    private Object dataHandle;

    private Method method;

    private Class<?> parameterType;

    private Type type;

    /**
     * 类型
     */
    public enum Type {
        //文本
        TEXT,
        //图片
        IMAGE
    }


    private HandlerBean(MConverter mConverter) throws Exception {
        Class<? extends IMxDataHandler> handleClass = mConverter.handleClass();
        if (!handleClass.isInterface() && StringUtils.isNotBlank(mConverter.method())) {
            dataHandle = handleClass.newInstance();
            method = dataHandle.getClass().getMethod(mConverter.method(), String.class);
            type = mConverter.type();
        }
    }

    public static HandlerBean create(MConverter dConverter) throws Exception {
        return new HandlerBean(dConverter);
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
