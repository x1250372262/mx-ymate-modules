package com.mx.ymate.security.dao;

import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.base.vo.SecurityUserListVO;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 11:31
 * @Description:
 */
public interface ISecurityUserDao {

    /**
     * 根据id查询
     *
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityUser findById(String id, String... fields) throws Exception;

    /**
     * 根据用户名和客户端查询
     *
     * @param userName
     * @param client
     * @return
     * @throws Exception
     */
    SecurityUser findByUserNameAndClient(String userName, String client) throws Exception;

    /**
     * 修改
     *
     * @param securityUser
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityUser update(SecurityUser securityUser, String... fields) throws Exception;

    /**
     * 添加
     *
     * @param securityUser
     * @return
     * @throws Exception
     */
    SecurityUser create(SecurityUser securityUser) throws Exception;

    /**
     * 查询所有
     * @param userName
     * @param realName
     * @param disableStatus
     * @param client
     * @param resourceId
     * @param page
     * @return
     * @throws Exception
     */
    IResultSet<SecurityUserListVO> findAll(String userName, String realName, Integer disableStatus, String client, String resourceId, Page page) throws Exception;

    /**
     * 根据条件查询
     * @param userName
     * @param client
     * @param resourceId
     * @return
     * @throws Exception
     */
    SecurityUser findByUserNameAndClientAndResourceId(String userName, String client, String resourceId) throws Exception;

}
