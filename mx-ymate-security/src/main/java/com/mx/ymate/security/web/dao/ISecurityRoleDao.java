package com.mx.ymate.security.web.dao;

import com.mx.ymate.security.base.model.SecurityRole;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:22
 * @Description:
 */
public interface ISecurityRoleDao {

    /**
     * 查询所有
     *
     * @param resourceId
     * @param client
     * @param name
     * @param page
     * @return
     * @throws Exception
     */
    IResultSet<SecurityRole> findAll(String resourceId, String client, String name, Page page) throws Exception;

    /**
     * 根据 resourceId client name查询
     *
     * @param resourceId
     * @param client
     * @param name
     * @return
     * @throws Exception
     */
    SecurityRole findByClientAndResourceIdAndName(String resourceId, String client, String name) throws Exception;

    /**
     * 根据 resourceId client name查询 排除id
     *
     * @param id
     * @param resourceId
     * @param client
     * @param name
     * @return
     * @throws Exception
     */
    SecurityRole findByClientAndResourceIdAndNameNotId(String id, String resourceId, String client, String name) throws Exception;

    /**
     * 添加
     *
     * @param securityRole
     * @return
     * @throws Exception
     */
    SecurityRole create(SecurityRole securityRole) throws Exception;

    /**
     * 修改
     *
     * @param securityRole
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityRole update(SecurityRole securityRole, String... fields) throws Exception;

    /**
     * 根据id查询
     *
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityRole findById(String id, String... fields) throws Exception;

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    int[] deleteByIds(String[] ids) throws Exception;
}
