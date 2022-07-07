package com.mx.ymate.security.base.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 菜单信息
 */
public class SecurityMenuListVO implements Serializable {

    private String text;

    private String state;

    private String icon;

    private Integer sort;

    @JSONField(name = "a_attr")
    private Map<Object, Object> attr;

    private List<SecurityMenuListVO> children;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SecurityMenuListVO> getChildren() {
        return children != null ? children : new ArrayList<>();
    }

    public void setChildren(List<SecurityMenuListVO> children) {
        this.children = children;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Map<Object, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<Object, Object> attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return "SecurityMenuListVO{" + "text='" + text + '\'' + ", state='" + state + '\'' + ", icon='" + icon + '\'' + ", sort=" + sort + ", attr=" + attr + ", children=" + children + '}';
    }
}
