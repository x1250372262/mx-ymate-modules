package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 10:42
 * @Description:
 */
public class SecurityPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String code;

    private String groupName;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "SecurityPermissionVO{" + "name='" + name + '\'' + ", code='" + code + '\'' + ", groupName='" + groupName + '\'' + '}';
    }
}
