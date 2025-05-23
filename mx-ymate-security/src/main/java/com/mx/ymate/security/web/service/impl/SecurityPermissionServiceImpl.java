package com.mx.ymate.security.web.service.impl;

import com.mx.ymate.dev.MxDev;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.vo.SecurityPermissionSelectVO;
import com.mx.ymate.security.base.vo.SecurityPermissionVO;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import com.mx.ymate.security.web.service.ISecurityPermissionService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.core.persistence.IResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityPermissionServiceImpl implements ISecurityPermissionService {

    @Inject
    private ISecurityPermissionDao iPermissionDao;

    @Override
    public MxResult select() throws Exception {
        List<SecurityPermissionSelectVO> permissionSelectVOList = new ArrayList<>();
        IResultSet<SecurityPermission> resultSet = iPermissionDao.findAll(Security.get().getConfig().client());
        if (!resultSet.isResultsAvailable()) {
            return MxResult.ok().data(permissionSelectVOList);
        }
        List<SecurityPermission> permissionList = resultSet.getResultData();
        Map<String, List<SecurityPermission>> permissionMap = permissionList.stream().collect(Collectors.groupingBy(SecurityPermission::getGroupName));
        for (Map.Entry<String, List<SecurityPermission>> entry : permissionMap.entrySet()) {
            SecurityPermissionSelectVO permissionGroupVO = new SecurityPermissionSelectVO();
            String groupName = entry.getValue().get(0).getGroupName();
            String groupI18nKey = entry.getValue().get(0).getGroupI18nKey();
            permissionGroupVO.setGroupName(I18nHelper.getMsg(groupI18nKey, groupName));
            List<SecurityPermissionVO> permissionVOList = BeanUtil.copyList(entry.getValue(), SecurityPermissionVO::new, (s, t) -> {
                t.setCode(s.getPermissionCode());
                t.setName(I18nHelper.getMsg(s.getPermissionI18nKey(), s.getPermissionName()));
            });
            permissionGroupVO.setPermissionVOList(permissionVOList);
            permissionSelectVOList.add(permissionGroupVO);
        }
        return MxResult.ok().data(permissionSelectVOList);
    }
}
