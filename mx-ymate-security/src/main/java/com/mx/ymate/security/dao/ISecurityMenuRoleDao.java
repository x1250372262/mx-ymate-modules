package com.mx.ymate.security.dao;

import com.mx.ymate.security.base.model.SecurityMenuRole;
import com.mx.ymate.security.base.vo.SecurityMenuRoleVO;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 13:36
 * @Description:
 */
public interface ISecurityMenuRoleDao {

    /**
     * 获取菜单角色
     *
     * @param menuId
     * @param resourceId
     * @param name
     * @param page
     * @return
     */
    IResultSet<SecurityMenuRoleVO> findAll(String menuId, String resourceId, String name, Page page) throws Exception;

    /**
     * 根据id查询
     *
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityMenuRole findById(String id, String... fields) throws Exception;

    /**
     * 根据菜单id 角色id 客户端查询
     *
     * @param menuId
     * @param roleId
     * @param client
     * @return
     * @throws Exception
     */
    SecurityMenuRole findByMenuIdAndRoleIdAndClient(String menuId, String roleId, String client) throws Exception;

    /**
     * 添加
     * @param securityMenuRole
     * @return
     * @throws Exception
     */
    SecurityMenuRole create(SecurityMenuRole securityMenuRole) throws Exception;

    /**
     * 批量删除
     * @param ids
     * @throws Exception
     */
    int[] deleteByIds(String[] ids) throws Exception;

}
