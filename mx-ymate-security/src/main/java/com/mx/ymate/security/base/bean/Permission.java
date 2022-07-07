package com.mx.ymate.security.base.bean;

/**
 * @Author: mengxiang.
 * @create: 2021-10-18 10:04
 * @Description:
 */
public class Permission {

    private String groupName;

    private String name;

    private String code;

    public Permission(String groupName, String name, String code) {
        this.groupName = groupName;
        this.name = name;
        this.code = code;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
