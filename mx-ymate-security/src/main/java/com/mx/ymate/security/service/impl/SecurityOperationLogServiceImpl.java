package com.mx.ymate.security.service.impl;

import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.base.vo.SecurityOperationLogListVO;
import com.mx.ymate.security.base.vo.SecurityOperationLogVO;
import com.mx.ymate.security.dao.ISecurityOperationLogDao;
import com.mx.ymate.security.handler.IUserHandler;
import com.mx.ymate.security.service.ISecurityOperationLogService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mengxiang.
 * @create: 2021-09-26 13:29
 * @Description:
 */
@Bean
public class SecurityOperationLogServiceImpl implements ISecurityOperationLogService {

    @Inject
    private ISecurityOperationLogDao iSecurityOperationLogDao;

    private final ISecurityConfig config = Security.get().getConfig();

    @Override
    public MxResult list(String title, Long startTime, Long endTime, PageBean pageBean) throws Exception {
        IUserHandler userHandler = config.userHandlerClass();
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.LOG), config.client());
        IResultSet<SecurityOperationLog> resultSet = iSecurityOperationLogDao.findAll(resourceId, config.client(), title, startTime, endTime, pageBean.toPage());
        return MxResult.ok().data(BeanUtil.copyResultSet(resultSet, SecurityOperationLogListVO::new));
    }

    @Override
    public MxResult detail(String id) throws Exception {
        return MxResult.ok().data(BeanUtil.copy(iSecurityOperationLogDao.findById(id), SecurityOperationLogVO::new));
    }

    @Override
    public MxResult delete(String[] ids) throws Exception {
        return MxResult.result(iSecurityOperationLogDao.deleteByIds(ids));
    }
}
