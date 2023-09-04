package com.mx.ymate.security.dao;

import com.mx.ymate.security.base.model.SecurityOperationLog;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 11:58
 * @Description:
 */
public interface ISecurityOperationLogDao {

    /**
     * 添加
     * @param securityOperationLog
     * @return
     * @throws Exception
     */
    SecurityOperationLog create(SecurityOperationLog securityOperationLog) throws Exception;

    /**
     * 查询所有
     * @param resourceId
     * @param client
     * @param title
     * @param startTime
     * @param endTime
     * @param page
     * @return
     * @throws Exception
     */
    IResultSet<SecurityOperationLog> findAll(String resourceId, String client, String title, Long startTime, Long endTime, Page page) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    SecurityOperationLog findById(String id,String...fields) throws Exception;

    /**
     * 批量删除
     * @param ids
     * @return
     * @throws Exception
     */
    int[] deleteByIds(String[] ids) throws Exception;
}
