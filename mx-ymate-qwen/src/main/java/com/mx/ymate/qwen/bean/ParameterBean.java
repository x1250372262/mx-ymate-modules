package com.mx.ymate.qwen.bean;

/**
 * @Author: xujianpeng.
 * @Date 2025/2/27.
 * @Time: 14:39.
 * @Description:
 */
public class ParameterBean {

    /**
     * 参数名 注解里面的
     */
    private String name;

    /**
     * 描述 注解里面的
     */
    private String description;

    /**
     * 参数类型
     */
    private String type;


    public ParameterBean(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
