package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 菜单信息
 */
public class SecurityMenuVO implements Serializable {

    private String id;

    private String parentId;

    private String name;

    private String icon;

    private String path;

    private String url;

    private Integer sort;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SecurityMenuVO{" + "id='" + id + '\'' + ", parentId='" + parentId + '\'' + ", name='" + name + '\'' + ", icon='" + icon + '\'' + ", path='" + path + '\'' + ", url='" + url + '\'' + ", sort=" + sort + ", type='" + type + '\'' + '}';
    }
}
