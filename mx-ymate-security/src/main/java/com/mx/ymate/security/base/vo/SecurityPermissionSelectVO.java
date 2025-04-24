package com.mx.ymate.security.base.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityPermissionSelectVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String groupName;

    private List<SecurityPermissionVO> permissionVOList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<SecurityPermissionVO> getPermissionVOList() {
        return permissionVOList;
    }

    public void setPermissionVOList(List<SecurityPermissionVO> permissionVOList) {
        this.permissionVOList = permissionVOList;
    }

    @Override
    public String toString() {
        return "SecurityPermissionSelectVO{" + "groupName='" + groupName + '\'' + ", permissionVOList=" + permissionVOList + '}';
    }
}
