package com.mx.ymate.monitor.service.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.support.page.Pages;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.monitor.dao.IServerDao;
import com.mx.ymate.monitor.model.Server;
import com.mx.ymate.monitor.service.IServerService;
import com.mx.ymate.monitor.vo.ServerListVO;
import com.mx.ymate.monitor.vo.ServerVO;
import com.mx.ymate.security.SaUtil;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;

/**
 * @Author: xujianpeng.
 * @Create: 2024/2/23 11:48
 * @Description:
 */
@Bean
public class ServerServiceImpl implements IServerService {

    @Inject
    private IServerDao iServerDao;

    @Override
    public MxResult create(String name, String ip, String user, String password, String remark) throws Exception {
        Server server = Server.builder()
                .id(UUIDUtils.UUID())
                .name(name)
                .ip(ip)
                .user(user)
                .password(password)
                .remark(remark)
                .createTime(System.currentTimeMillis())
                .createUser(SaUtil.loginId())
                .lastModifyTime(System.currentTimeMillis())
                .lastModifyUser(SaUtil.loginId())
                .build();
        server = iServerDao.create(server);
        return MxResult.result(server);
    }

    @Override
    public MxResult update(String id, String name, String ip, String user, String password, String remark) throws Exception {
        Server server = iServerDao.findById(id, Server.FIELDS.ID);
        if (server == null) {
            return MxResult.noData();
        }
        server = server.bind()
                .name(name)
                .ip(ip)
                .user(user)
                .password(password)
                .remark(remark)
                .lastModifyTime(System.currentTimeMillis())
                .lastModifyUser(SaUtil.loginId())
                .build();
        server = iServerDao.update(server, Server.FIELDS.NAME, Server.FIELDS.IP, Server.FIELDS.USER, Server.FIELDS.PASSWORD, Server.FIELDS.REMARK);
        return MxResult.result(server);
    }

    @Override
    public MxResult delete(String[] ids) throws Exception {
        int[] result = iServerDao.delete(ids);
        return MxResult.result(result);
    }

    @Override
    public MxResult detail(String id) throws Exception {
        Server server = iServerDao.findById(id);
        ServerVO serverVO = BeanUtil.copy(server, ServerVO::new);
        return MxResult.okData(serverVO);
    }

    @Override
    public MxResult list(String name, String ip, PageBean pageBean) throws Exception {
        IResultSet<ServerListVO> resultSet = iServerDao.findAll(name, ip, pageBean);
        return MxResult.okData(Pages.create(resultSet));
    }

}
