package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityUserRoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String roleId;

    private String roleName;

    private Long createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

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

    @Override
    public String toString() {
        return "SecurityUserRoleVO{" + "id='" + id + '\'' + ", userId='" + userId + '\'' + ", roleId='" + roleId + '\'' + ", roleName='" + roleName + '\'' + ", createTime=" + createTime + '}';
    }
}
