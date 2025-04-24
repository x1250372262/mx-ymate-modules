package com.mx.ymate.security.web.service;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityUserRoleService {

    /**
     * 获取所有权限
     *
     * @param securityUserId
     * @return
     * @throws Exception
     */
    List<String> securityUserPermissionList(String securityUserId) throws Exception;


}
