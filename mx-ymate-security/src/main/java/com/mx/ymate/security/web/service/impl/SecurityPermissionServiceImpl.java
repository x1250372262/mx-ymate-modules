package com.mx.ymate.security.web.service.impl;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.vo.SecurityPermissionSelectVO;
import com.mx.ymate.security.base.vo.SecurityPermissionVO;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import com.mx.ymate.security.handler.IUserHandler;
import com.mx.ymate.security.web.service.ISecurityPermissionService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: mengxiang.
 * @create: 2021-09-24 14:45
 * @Description:
 */
@Bean
public class SecurityPermissionServiceImpl implements ISecurityPermissionService {

    @Inject
    private ISecurityPermissionDao iPermissionDao;
    private final ISecurityConfig config = Security.get().getConfig();
    private final IUserHandler userHandler = config.userHandlerClass();

    @Override
    public MxResult list() throws Exception {
        String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.PERMISSION, null), config.client());
        List<SecurityPermissionSelectVO> permissionSelectVOList = new ArrayList<>();
        IResultSet<SecurityPermission> resultSet = iPermissionDao.findAll(config.client(), resourceId);
        if (!resultSet.isResultsAvailable()) {
            return MxResult.ok().data(permissionSelectVOList);
        }
        List<SecurityPermission> permissionList = resultSet.getResultData();
        Map<String, List<SecurityPermission>> permissionMap = permissionList.stream().collect(Collectors.groupingBy(SecurityPermission::getGroupName));
        for (Map.Entry<String, List<SecurityPermission>> entry : permissionMap.entrySet()) {
            SecurityPermissionSelectVO permissionGroupVO = new SecurityPermissionSelectVO();
            permissionGroupVO.setGroupName(entry.getKey());
            List<SecurityPermissionVO> permissionVOList = BeanUtil.copyList(entry.getValue(), SecurityPermissionVO::new, (s, t) -> {
                t.setCode(s.getPermissionCode());
                t.setName(s.getPermissionName());
            });
            permissionGroupVO.setPermissionVOList(permissionVOList);
            permissionSelectVOList.add(permissionGroupVO);
        }
        return MxResult.ok().data(permissionSelectVOList);
    }
}
