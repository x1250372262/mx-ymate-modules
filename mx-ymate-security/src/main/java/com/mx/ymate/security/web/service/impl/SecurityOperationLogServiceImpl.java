package com.mx.ymate.security.web.service.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.base.vo.SecurityOperationLogListVO;
import com.mx.ymate.security.base.vo.SecurityOperationLogVO;
import com.mx.ymate.security.handler.IResourceHandler;
import com.mx.ymate.security.web.dao.ISecurityOperationLogDao;
import com.mx.ymate.security.web.service.ISecurityOperationLogService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import org.apache.commons.lang3.StringUtils;

import static com.mx.ymate.security.I18nConstant.LOG_OPERATION_LOG_DELETE_TITLE_I18N_KEY;
import static com.mx.ymate.security.I18nConstant.LOG_OPERATION_LOG_DELETE_TITLE_MSG;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityOperationLogServiceImpl implements ISecurityOperationLogService {

    @Inject
    private ISecurityOperationLogDao iSecurityOperationLogDao;

    private final ISecurityConfig config = Security.get().getConfig();

    @Override
    public MxResult list(String title, Long startTime, Long endTime, PageBean pageBean) throws Exception {
        IResourceHandler resourceHandler = config.resourceHandlerClass();
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.LOG, SaUtil.loginId()), config.client());
        IResultSet<SecurityOperationLog> resultSet = iSecurityOperationLogDao.findAll(resourceId, config.client(), title, startTime, endTime, pageBean.toPage());
        return MxResult.ok().data(BeanUtil.copyResultSet(resultSet, SecurityOperationLogListVO::new));
    }

    @Override
    public MxResult detail(String id) throws Exception {
        return MxResult.ok().data(BeanUtil.copy(iSecurityOperationLogDao.findById(id), SecurityOperationLogVO::new));
    }

    @Override
    @OperationLog(operationType = OperationType.DELETE, title = LOG_OPERATION_LOG_DELETE_TITLE_MSG,i18nKey = LOG_OPERATION_LOG_DELETE_TITLE_I18N_KEY)
    public MxResult delete(String[] ids) throws Exception {
        return MxResult.result(iSecurityOperationLogDao.deleteByIds(ids));
    }
}
