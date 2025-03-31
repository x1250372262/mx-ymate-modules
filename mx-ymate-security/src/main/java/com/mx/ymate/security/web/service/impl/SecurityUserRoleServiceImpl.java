package com.mx.ymate.security.web.service.impl;

import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.vo.SecurityUserPermissionVO;
import com.mx.ymate.security.handler.IUserHandler;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import com.mx.ymate.security.web.dao.ISecurityUserRoleDao;
import com.mx.ymate.security.web.service.ISecurityUserRoleService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 16:02
 * @Description:
 */
@Bean
public class SecurityUserRoleServiceImpl implements ISecurityUserRoleService {

    @Inject
    private ISecurityUserRoleDao iSecurityUserRoleDao;
    @Inject
    private ISecurityPermissionDao isecurityPermissionDao;

    private final ISecurityConfig config = Security.get().getConfig();
    private final IUserHandler userHandler = config.userHandlerClass();

    @Override
    public List<String> securityUserPermissionList(String securityUserId) throws Exception {
        String resourceId;
        List<String> permissionList = new ArrayList<>();
        if (SaUtil.isFounder(securityUserId)) {
            resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.PERMISSION, null), config.client());
            IResultSet<SecurityPermission> resultSet = isecurityPermissionDao.findAll(config.client(), resourceId);
            if (resultSet.isResultsAvailable()) {
                List<SecurityPermission> permissions = resultSet.getResultData();
                permissions.forEach(permission -> permissionList.add(permission.getPermissionCode()));
            }
        } else {
            resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.USER, securityUserId), config.client());
            IResultSet<SecurityUserPermissionVO> resultSet = iSecurityUserRoleDao.permissionList(securityUserId, config.client(), resourceId);
            if (resultSet.isResultsAvailable()) {
                List<SecurityUserPermissionVO> securityUserPermissionVOList = resultSet.getResultData();
                securityUserPermissionVOList.forEach(securityUserPermissionVO -> permissionList.add(securityUserPermissionVO.getPermissionCode()));
            }
        }
        return permissionList;
    }
}
