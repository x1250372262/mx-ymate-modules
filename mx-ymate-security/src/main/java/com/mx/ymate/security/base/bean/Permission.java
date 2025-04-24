package com.mx.ymate.security.base.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupName;

    private String groupI18nKey;

    private String name;

    private String nameI18nKey;

    private String code;

    public Permission(String groupName, String groupI18nKey, String name, String nameI18nKey, String code) {
        this.groupName = groupName;
        this.groupI18nKey = groupI18nKey;
        this.name = name;
        this.nameI18nKey = nameI18nKey;
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

    public String getGroupI18nKey() {
        return groupI18nKey;
    }

    public void setGroupI18nKey(String groupI18nKey) {
        this.groupI18nKey = groupI18nKey;
    }

    public String getNameI18nKey() {
        return nameI18nKey;
    }

    public void setNameI18nKey(String nameI18nKey) {
        this.nameI18nKey = nameI18nKey;
    }
}
