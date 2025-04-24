package com.mx.ymate.security.web.service;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.security.base.bean.SecurityRoleBean;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityRoleService {


    /**
     * 角色列表
     *
     * @param name
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult list(String name, PageBean pageBean) throws Exception;

    /**
     * 添加角色
     *
     * @param roleBean
     * @return
     * @throws Exception
     */
    MxResult create(SecurityRoleBean roleBean) throws Exception;

    /**
     * 修改角色
     *
     * @param id
     * @param lastModifyTime
     * @param roleBean
     * @return
     * @throws Exception
     */
    MxResult update(String id, Long lastModifyTime, SecurityRoleBean roleBean) throws Exception;

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult detail(String id) throws Exception;

    /**
     * 删除角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MxResult delete(String[] ids) throws Exception;

    /**
     * 角色权限列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult permissionList(String id) throws Exception;

    /**
     * 角色权限绑定
     *
     * @param id
     * @param permissions
     * @return
     * @throws Exception
     */
    MxResult permissionBind(String id, String[] permissions) throws Exception;
}
