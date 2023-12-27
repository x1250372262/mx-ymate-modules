package com.mx.ymate.security.base.vo;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 10:42
 * @Description:
 */
public class SecurityRoleListVO extends SecurityRoleVO {

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
