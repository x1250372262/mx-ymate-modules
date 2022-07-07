package com.mx.ymate.security.service;

import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.security.base.bean.SecurityMenuBean;

/**
 * @Author: mengxiang.
 * @create: 2021-09-23 15:41
 * @Description:
 */
public interface ISecurityMenuService {

    /**
     * 左侧导航栏
     *
     * @return
     * @throws Exception
     */
    MxResult nav() throws Exception;

    /**
     * 菜单列表
     *
     * @return
     * @throws Exception
     */
    MxResult list() throws Exception;

    /**
     * 添加菜单
     *
     * @param menuBean
     * @return
     * @throws Exception
     */
    MxResult create(SecurityMenuBean menuBean) throws Exception;

    /**
     * 修改菜单
     *
     * @param id
     * @param menuBean
     * @return
     * @throws Exception
     */
    MxResult update(String id, SecurityMenuBean menuBean) throws Exception;

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult detail(String id) throws Exception;

    /**
     * 删除菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult delete(String id) throws Exception;

    /**
     * 菜单角色列表
     *
     * @param menuId
     * @param name
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult roleList(String menuId, String name, PageBean pageBean) throws Exception;

    /**
     * 添加菜单角色
     *
     * @param menuId
     * @param roleId
     * @return
     * @throws Exception
     */
    MxResult roleCreate(String menuId, String roleId) throws Exception;

    /**
     * 删除菜单角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MxResult roleDelete(String[] ids) throws Exception;
}
