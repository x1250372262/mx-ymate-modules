package com.mx.ymate.security.service;

import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 16:02
 * @Description:
 */
public interface ISecurityUserRoleService {

    /**
     * 获取所有权限
     *
     * @param securityUserId
     * @param token
     * @return
     * @throws Exception
     */
    List<String> securityUserPermissionList(String securityUserId, String token) throws Exception;


}
