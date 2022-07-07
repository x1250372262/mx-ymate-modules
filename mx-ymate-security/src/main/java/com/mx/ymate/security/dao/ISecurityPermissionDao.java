package com.mx.ymate.security.dao;

import com.mx.ymate.security.base.model.SecurityPermission;
import net.ymate.platform.core.persistence.IResultSet;

/**
 * @Author: 徐建鹏.
 * @create: 2022-07-04 14:11
 * @Description:
 */
public interface ISecurityPermissionDao {

    /**
     * 查询所有
     *
     * @param client
     * @param resourceId
     * @return
     * @throws Exception
     */
    IResultSet<SecurityPermission> findAll(String client, String resourceId) throws Exception;
}
