package com.mx.ymate.security.service;

import com.mx.ymate.dev.result.MxResult;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 14:45
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
