package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityMenuBean;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

import static com.mx.ymate.security.I18nConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityMenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @VMxRequired(msg = PARENT_ID_NOT_EMPTY_MSG, i18nKey = PARENT_ID_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private String parentId;

    @VMxRequired(msg = NAME_NOT_EMPTY_MSG, i18nKey = NAME_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private String name;

    @RequestParam
    private String i18nKey;

    @RequestParam
    private String icon;

    @RequestParam
    private String path;

    @RequestParam
    private String url;

    @VMxRequired(msg = SORT_NOT_EMPTY_MSG, i18nKey = SORT_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private Integer sort;

    @VMxRequired(msg = TYPE_NOT_EMPTY_MSG, i18nKey = TYPE_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private Integer type;

    @VMxRequired(msg = HIDE_NOT_EMPTY_MSG, i18nKey = HIDE_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private Integer hideStatus;

    @RequestParam
    private String permission;

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

    public String getI18nKey() {
        return i18nKey;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
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
        return "SecurityMenuDTO{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", i18nKey='" + i18nKey + '\'' +
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
