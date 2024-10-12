package com.mx.ymate.security.base.vo;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 16:00
 * @Description:
 */
public class SecurityUserListVO extends SecurityUserVO {

    private String roleName;

    private Long createTime;

    private Integer disableStatus;

    private Integer loginLockStatus;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDisableStatus() {
        return disableStatus;
    }

    public void setDisableStatus(Integer disableStatus) {
        this.disableStatus = disableStatus;
    }

    public Integer getLoginLockStatus() {
        return loginLockStatus;
    }

    public void setLoginLockStatus(Integer loginLockStatus) {
        this.loginLockStatus = loginLockStatus;
    }

    @Override
    public String toString() {
        return "SecurityUserListVO{" +
                "roleName='" + roleName + '\'' +
                ", createTime=" + createTime +
                ", disableStatus=" + disableStatus +
                ", loginLockStatus=" + loginLockStatus +
                "} " + super.toString();
    }
}
