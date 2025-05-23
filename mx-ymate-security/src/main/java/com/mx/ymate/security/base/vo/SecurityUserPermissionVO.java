package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityUserPermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String permissionId;

    private String userId;

    private String permissionName;

    private String permissionCode;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    @Override
    public String toString() {
        return "SecurityUserPermissionVO{" + "permissionId='" + permissionId + '\'' + ", userId='" + userId + '\'' + ", permissionName='" + permissionName + '\'' + ", permissionCode='" + permissionCode + '\'' + '}';
    }
}
