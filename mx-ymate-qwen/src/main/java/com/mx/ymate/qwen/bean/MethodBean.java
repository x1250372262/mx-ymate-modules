package com.mx.ymate.qwen.bean;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author: xujianpeng.
 * @Date 2025/2/27.
 * @Time: 14:39.
 * @Description:
 */
public class MethodBean {

    /**
     * 方法名 注解里面的
     */
    private String name;

    /**
     * 描述 注解里面的
     */
    private String description;

    /**
     * 方法
     */
    private Method method;

    /**
     * 方法参数
     */
    private List<ParameterBean> parameters;

    /**
     * ai返回参数
     */
    private Map<String,Object> parameterMap;

    /**
     * 类的实例化
     */
    private Object object;

    public MethodBean(String name, String description, Method method, Object object) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<ParameterBean> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterBean> parameters) {
        this.parameters = parameters;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
