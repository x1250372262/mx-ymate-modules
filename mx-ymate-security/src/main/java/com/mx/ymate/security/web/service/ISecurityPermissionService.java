package com.mx.ymate.security.web.service;

import com.mx.ymate.dev.support.mvc.MxResult;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityPermissionService {

    /**
     * 权限列表
     *
     * @return
     * @throws Exception
     */
    MxResult list() throws Exception;
}
