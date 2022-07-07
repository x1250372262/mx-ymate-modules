package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 10:42
 * @Description:
 */
public class SecurityRoleVO implements Serializable {

    private String id;

    private String name;

    private String remark;

    private Long lastModifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public String toString() {
        return "SecurityRoleVO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", remark='" + remark + '\'' + ", lastModifyTime=" + lastModifyTime + '}';
    }
}
