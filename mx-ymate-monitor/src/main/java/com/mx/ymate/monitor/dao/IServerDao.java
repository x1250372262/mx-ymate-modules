package com.mx.ymate.monitor.dao;

import com.mx.ymate.dev.support.jdbc.dao.IMxDao;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.monitor.model.Server;
import com.mx.ymate.monitor.vo.ServerListVO;
import net.ymate.platform.core.persistence.IResultSet;

/**
 * @Author: xujianpeng.
 * @Create: 2024/2/23 11:50
 * @Description:
 */
public interface IServerDao extends IMxDao<Server> {

    /**
     * 查询所有
     * @param name
     * @param ip
     * @param pageBean
     * @return
     * @throws Exception
     */
    IResultSet<ServerListVO> findAll(String name, String ip, PageBean pageBean) throws Exception;
}
