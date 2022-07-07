package com.mx.ymate.security.base.vo;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 15:45
 * @Description:
 */
public class SecurityUserPermissionVO implements Serializable {

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
