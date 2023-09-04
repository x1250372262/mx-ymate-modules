package com.mx.ymate.security.dao;

import com.mx.ymate.security.base.model.SecurityUserRole;
import com.mx.ymate.security.base.vo.SecurityUserPermissionVO;
import com.mx.ymate.security.base.vo.SecurityUserRoleVO;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;

import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:42
 * @Description:
 */
public interface ISecurityUserRoleDao {

    /**
     * 根据roleid批量删除
     *
     * @param roleIds
     * @return
     * @throws Exception
     */
    void deleteByRoleIds(List<String> roleIds) throws Exception;

    /**
     * 添加
     * @param securityUserRole
     * @return
     * @throws Exception
     */
    SecurityUserRole create(SecurityUserRole securityUserRole) throws Exception;

    /**
     * 批量删除
     * @param ids
     * @return
     * @throws Exception
     */
    int[] deleteByIds(String[] ids) throws Exception;

    /**
     * 获取用户权限列表
     *
     * @param userId
     * @param client
     * @param resourceId
     * @return
     * @throws Exception
     */
    IResultSet<SecurityUserPermissionVO> permissionList(String userId, String client, String resourceId) throws Exception;


    /**
     * 获取用户角色列表
     *
     * @param userId
     * @param client
     * @param resourceId
     * @param page
     * @return
     * @throws Exception
     */
    IResultSet<SecurityUserRoleVO> roleList(String userId, String client, String resourceId, Page page) throws Exception;

    /**
     * 根据条件查询
     *
     * @param userId
     * @param roleId
     * @param client
     * @param resourceId
     * @return
     * @throws Exception
     */
    SecurityUserRole findByUserIdAndRoleidAndClientAndResourceId(String userId,String roleId, String client, String resourceId) throws Exception;


}
