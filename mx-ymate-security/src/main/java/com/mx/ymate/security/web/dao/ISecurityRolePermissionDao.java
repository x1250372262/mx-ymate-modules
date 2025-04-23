package com.mx.ymate.security.web.dao;

import com.mx.ymate.security.base.model.SecurityRolePermission;
import net.ymate.platform.core.persistence.IResultSet;

import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:49
 * @Description:
 */
public interface ISecurityRolePermissionDao {

    /**
     * 查询所有
     *
     * @param client
     * @param roleId
     * @return
     * @throws Exception
     */
    IResultSet<SecurityRolePermission> findAll(String client, String roleId) throws Exception;

    /**
     * 批量添加
     *
     * @param list
     * @return
     * @throws Exception
     */
    void createAll(List<SecurityRolePermission> list) throws Exception;


    /**
     * 按条件删除
     *
     * @param client
     * @param roleId
     * @throws Exception
     */
    void deleteByClientAndRoleIdAndResourceId(String client, String roleId) throws Exception;

}
