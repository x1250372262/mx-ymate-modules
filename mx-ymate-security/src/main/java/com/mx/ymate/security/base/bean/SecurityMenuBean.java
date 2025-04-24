package com.mx.ymate.security.base.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityMenuBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String parentId;

    private String name;

    private String icon;

    private String path;

    private String url;

    private Integer sort;

    private Integer type;

    private Integer hideStatus;

    private String permission;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHideStatus() {
        return hideStatus;
    }

    public void setHideStatus(Integer hideStatus) {
        this.hideStatus = hideStatus;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "SecurityMenuBean{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", sort=" + sort +
                ", type=" + type +
                ", hideStatus=" + hideStatus +
                ", permission='" + permission + '\'' +
                '}';
    }
}
