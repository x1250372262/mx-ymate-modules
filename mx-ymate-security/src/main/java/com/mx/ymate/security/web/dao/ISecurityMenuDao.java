package com.mx.ymate.security.web.dao;

import com.mx.ymate.security.base.model.SecurityMenu;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import net.ymate.platform.core.persistence.IResultSet;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 13:03
 * @Description:
 */
public interface ISecurityMenuDao {

    /**
     * 根据类型和客户端获取
     * @param type
     * @param hideStatus
     * @param client
     * @param resourceId
     * @return
     * @throws Exception
     */
    IResultSet<SecurityMenuNavVO> findAll(Integer type, Integer hideStatus, String client, String resourceId) throws Exception;


    /**
     * 添加
     *
     * @param securityMenu
     * @return
     * @throws Exception
     */
    SecurityMenu create(SecurityMenu securityMenu) throws Exception;

    /**
     * 修改
     *
     * @param securityMenu
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityMenu update(SecurityMenu securityMenu, String... fields) throws Exception;

    /**
     * 根据id查询
     *
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityMenu findById(String id, String... fields) throws Exception;

    /**
     * 根据父id查询
     *
     * @param parentId
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityMenu findByParentId(String parentId, String... fields) throws Exception;

    /**
     * 根据id删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    int delete(String id) throws Exception;
}
