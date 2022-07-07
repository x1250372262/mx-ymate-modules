package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 10:42
 * @Description:
 */
public class SecurityRoleListVO extends SecurityRoleVO implements Serializable {

    private Long createTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SecurityRoleListVO{" + "createTime=" + createTime + "} " + super.toString();
    }
}
