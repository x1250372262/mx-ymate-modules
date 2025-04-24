package com.mx.ymate.security.web.service.impl;

import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.vo.SecurityUserPermissionVO;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import com.mx.ymate.security.web.dao.ISecurityUserRoleDao;
import com.mx.ymate.security.web.service.ISecurityUserRoleService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityUserRoleServiceImpl implements ISecurityUserRoleService {

    @Inject
    private ISecurityUserRoleDao iSecurityUserRoleDao;
    @Inject
    private ISecurityPermissionDao isecurityPermissionDao;

    private final ISecurityConfig config = Security.get().getConfig();


    @Override
    public List<String> securityUserPermissionList(String securityUserId) throws Exception {
        List<String> permissionList = new ArrayList<>();
        if (SaUtil.isFounder(securityUserId)) {
            IResultSet<SecurityPermission> resultSet = isecurityPermissionDao.findAll(config.client());
            if (resultSet.isResultsAvailable()) {
                List<SecurityPermission> permissions = resultSet.getResultData();
                permissions.forEach(permission -> permissionList.add(permission.getPermissionCode()));
            }
        } else {
            IResultSet<SecurityUserPermissionVO> resultSet = iSecurityUserRoleDao.permissionList(securityUserId, config.client());
            if (resultSet.isResultsAvailable()) {
                List<SecurityUserPermissionVO> securityUserPermissionVOList = resultSet.getResultData();
                securityUserPermissionVOList.forEach(securityUserPermissionVO -> permissionList.add(securityUserPermissionVO.getPermissionCode()));
            }
        }
        return permissionList;
    }
}
