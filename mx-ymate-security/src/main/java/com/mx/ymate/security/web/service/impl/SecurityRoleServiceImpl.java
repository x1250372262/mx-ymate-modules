package com.mx.ymate.security.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.dev.support.page.Pages;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.bean.SecurityRoleBean;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.model.SecurityRole;
import com.mx.ymate.security.base.model.SecurityRolePermission;
import com.mx.ymate.security.base.vo.SecurityRoleVO;
import com.mx.ymate.security.handler.IResourceHandler;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import com.mx.ymate.security.web.dao.ISecurityRoleDao;
import com.mx.ymate.security.web.dao.ISecurityRolePermissionDao;
import com.mx.ymate.security.web.dao.ISecurityUserRoleDao;
import com.mx.ymate.security.web.service.ISecurityRoleService;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.annotation.Transaction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mx.ymate.security.I18nConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
@Transaction
public class SecurityRoleServiceImpl implements ISecurityRoleService {

    @Inject
    private ISecurityRoleDao iSecurityRoleDao;
    @Inject
    private ISecurityUserRoleDao iSecurityUserRoleDao;
    @Inject
    private ISecurityRolePermissionDao iSecurityRolePermissionDao;
    @Inject
    private ISecurityPermissionDao isecurityPermissionDao;

    private final ISecurityConfig config = Security.get().getConfig();

    private final IResourceHandler resourceHandler = config.resourceHandlerClass();

    @Override
    public MxResult list(String name, PageBean pageBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.ROLE, SaUtil.loginId()), config.client());
        IResultSet<SecurityRole> resultSet = iSecurityRoleDao.findAll(resourceId, config.client(), name, pageBean.toPage());
        return MxResult.ok().data(Pages.create(resultSet));
    }

    @Override
    @OperationLog(operationType = OperationType.CREATE, title = LOG_ROLE_CREATE_TITLE_MSG, i18nKey = LOG_ROLE_CREATE_TITLE_I18N_KEY)
    public MxResult create(SecurityRoleBean roleBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.ROLE, SaUtil.loginId()), config.client());
        SecurityRole role = iSecurityRoleDao.findByClientAndResourceIdAndName(resourceId, config.client(), roleBean.getName());
        if (role != null) {
            return MxResult.sameName();
        }
        role = BeanUtil.copy(roleBean, SecurityRole::new);
        role.setId(UUIDUtils.UUID());
        role.setClient(config.client());
        role.setResourceId(resourceId);
        role.setCreateTime(DateTimeUtils.currentTimeMillis());
        role.setCreateUser(SaUtil.loginId());
        role.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        role.setLastModifyUser(SaUtil.loginId());
        role = iSecurityRoleDao.create(role);
        return MxResult.result(role);
    }

    @Override
    @OperationLog(operationType = OperationType.UPDATE, title = LOG_ROLE_UPDATE_TITLE_MSG, i18nKey = LOG_ROLE_UPDATE_TITLE_I18N_KEY)
    public MxResult update(String id, Long lastModifyTime, SecurityRoleBean roleBean) throws Exception {
        String resourceId = StringUtils.defaultIfBlank(resourceHandler.buildResourceId(ResourceType.ROLE, SaUtil.loginId()), config.client());
        SecurityRole role = iSecurityRoleDao.findByClientAndResourceIdAndNameNotId(id, resourceId, config.client(), roleBean.getName());
        if (role != null) {
            return MxResult.sameName();
        }
        role = iSecurityRoleDao.findById(id);
        if (role == null) {
            return MxResult.noData();
        }
        if (MxResult.checkVersion(role.getLastModifyTime(), lastModifyTime)) {
            return MxResult.noVersion();
        }
        role = BeanUtil.duplicate(roleBean, role);
        role.setLastModifyTime(DateTimeUtils.currentTimeMillis());
        role.setLastModifyUser(SaUtil.loginId());
        role = iSecurityRoleDao.update(role, SecurityRole.FIELDS.NAME, SecurityRole.FIELDS.REMARK, SecurityRole.FIELDS.LAST_MODIFY_TIME, SecurityRole.FIELDS.LAST_MODIFY_USER);
        return MxResult.result(role);
    }

    @Override
    public MxResult detail(String id) throws Exception {
        return MxResult.ok().data(BeanUtil.copy(iSecurityRoleDao.findById(id), SecurityRoleVO::new));
    }

    @Override
    @Transaction
    @OperationLog(operationType = OperationType.DELETE, title = LOG_ROLE_DELETE_TITLE_MSG, i18nKey = LOG_ROLE_DELETE_TITLE_I18N_KEY)
    public MxResult delete(String[] ids) throws Exception {
        iSecurityUserRoleDao.deleteByRoleIds(Arrays.asList(ids));
        return MxResult.result(iSecurityRoleDao.deleteByIds(ids));
    }

    @Override
    public MxResult permissionList(String id) throws Exception {
        JSONObject jsonObject = new JSONObject();
        SecurityRole role = iSecurityRoleDao.findById(id);
        if (role == null) {
            return MxResult.noData();
        }
        IResultSet<SecurityRolePermission> resultSet = iSecurityRolePermissionDao.findAll(config.client(), id);
        if (resultSet.isResultsAvailable()) {
            List<SecurityRolePermission> rolePermissionList = resultSet.getResultData();
            String[] selectRoles = new String[rolePermissionList.size()];
            for (int i = 0; i < rolePermissionList.size(); i++) {
                selectRoles[i] = rolePermissionList.get(i).getPermissionCode();
            }
            jsonObject.put("permissions", selectRoles);
        }
        return MxResult.ok().data(jsonObject);
    }

    @Override
    @Transaction
    @OperationLog(operationType = OperationType.OTHER, title = LOG_ROLE_PERMISSION_BIND_TITLE_MSG, i18nKey = LOG_ROLE_PERMISSION_BIND_TITLE_I18N_KEY)
    public MxResult permissionBind(String id, String[] permissions) throws Exception {
        SecurityRole role = iSecurityRoleDao.findById(id);
        if (role == null) {
            return MxResult.noData();
        }
        iSecurityRolePermissionDao.deleteByClientAndRoleIdAndResourceId(config.client(), id);
        List<SecurityPermission> permissionList = isecurityPermissionDao.findAll(config.client()).getResultData();
        if (permissions != null && permissions.length > 0) {
            List<SecurityRolePermission> rolePermissions = new ArrayList<>();
            for (String permissionCode : permissions) {
                Optional<SecurityPermission> optional = permissionList.stream().filter(p -> p.getPermissionCode().equals(permissionCode)).findFirst();
                if (!optional.isPresent()) {
                    continue;
                }
                SecurityPermission permission = optional.get();
                SecurityRolePermission rolePermission = SecurityRolePermission.builder()
                        .id(UUIDUtils.UUID())
                        .roleId(id)
                        .client(config.client())
                        .permissonId(permission.getId())
                        .createTime(DateTimeUtils.currentTimeMillis())
                        .groupName(permission.getGroupName())
                        .permissionName(permission.getPermissionName())
                        .permissionCode(permission.getPermissionCode())
                        .build();
                rolePermissions.add(rolePermission);
            }
            if (CollUtil.isNotEmpty(rolePermissions)) {
                iSecurityRolePermissionDao.createAll(rolePermissions);
            }
        }
        return MxResult.ok();
    }
}
