package com.mx.ymate.monitor.service;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;

/**
 * @Author: mengxiang.
 * @Create: 2024/2/23 11:43
 * @Description:
 */
public interface IServerService {

    /**
     * 添加
     *
     * @param name
     * @param ip
     * @param user
     * @param password
     * @param remark
     * @return
     * @throws Exception
     */
    MxResult create(String name, String ip, String user, String password, String remark) throws Exception;

    /**
     * 修改
     *
     * @param id
     * @param name
     * @param ip
     * @param user
     * @param password
     * @param remark
     * @return
     * @throws Exception
     */
    MxResult update(String id, String name, String ip, String user, String password, String remark) throws Exception;

    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    MxResult delete(String[] ids) throws Exception;

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    MxResult detail(String id) throws Exception;

    /**
     * 列表
     *
     * @param name
     * @param ip
     * @param pageBean
     * @return
     * @throws Exception
     */
    MxResult list(String name, String ip, PageBean pageBean) throws Exception;
}
