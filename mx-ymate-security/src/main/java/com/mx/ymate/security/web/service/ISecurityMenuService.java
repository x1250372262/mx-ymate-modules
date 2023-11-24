package com.mx.ymate.security.web.service;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.bean.SecurityMenuBean;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;

import java.util.List;

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
     * 左侧导航栏数据
     *
     * @return
     * @throws Exception
     */
    List<SecurityMenuNavVO> navList(String userId,List<String> permissionList, boolean isFounder) throws Exception;

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

}
