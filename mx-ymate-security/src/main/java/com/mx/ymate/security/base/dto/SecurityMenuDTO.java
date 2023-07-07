package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityMenuBean;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 菜单信息
 */
public class SecurityMenuDTO implements Serializable {

    @VRequired(msg = "父ID不能为空")
    @RequestParam
    private String parentId;

    @VRequired(msg = "名称不能为空")
    @RequestParam
    private String name;

    @RequestParam
    private String icon;

    @RequestParam
    private String path;

    @RequestParam
    private String url;

    @VRequired(msg = "排序不能为空")
    @RequestParam
    private Integer sort;

    @VRequired(msg = "类型不能为空")
    @RequestParam
    private Integer type;

    @VRequired(msg = "是否隐藏不能为空")
    @RequestParam
    private Integer hideStatus;

    public SecurityMenuBean toBean() throws Exception {
        return BeanUtil.copy(this, SecurityMenuBean::new);
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

    @Override
    public String toString() {
        return "SecurityMenuDTO{" + "parentId='" + parentId + '\'' + ", name='" + name + '\'' + ", icon='" + icon + '\'' + ", path='" + path + '\'' + ", url='" + url + '\'' + ", sort=" + sort + ", type=" + type + '}';
    }
}
