package com.mx.ymate.security.web.service;


import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;

/**
 * @Author: mengxiang.
 * @create: 2021-09-23 15:41
 * @Description:
 */
public interface ISecurityOperationLogService {


    /**
     * 日志列表
     *
     * @param title
     * @param startTime
     * @param endTime
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult list(String title, Long startTime, Long endTime, PageBean pageBean) throws Exception;

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult detail(String id) throws Exception;

    /**
     * 删除日志
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MxResult delete(String[] ids) throws Exception;
}
