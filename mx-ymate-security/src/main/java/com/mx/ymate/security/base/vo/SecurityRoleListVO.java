package com.mx.ymate.security.base.vo;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
