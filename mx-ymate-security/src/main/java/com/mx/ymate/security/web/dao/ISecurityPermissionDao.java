package com.mx.ymate.security.web.dao;

import com.mx.ymate.security.base.model.SecurityPermission;
import net.ymate.platform.core.persistence.IResultSet;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityPermissionDao {

    /**
     * 查询所有
     *
     * @param client
     * @return
     * @throws Exception
     */
    IResultSet<SecurityPermission> findAll(String client) throws Exception;
}
