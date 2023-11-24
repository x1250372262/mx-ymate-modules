package com.mx.ymate.security.web.dao.impl;

import com.mx.ymate.security.base.model.SecurityRolePermission;
import com.mx.ymate.security.web.dao.ISecurityRolePermissionDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;

import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:50
 * @Description:
 */
@Bean
public class SecurityRolePermissionDaoImpl implements ISecurityRolePermissionDao {
    @Override
    public IResultSet<SecurityRolePermission> findAll(String client, String resourceId, String roleId) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityRolePermission.FIELDS.CLIENT).param(client)
                .and().eqWrap(SecurityRolePermission.FIELDS.RESOURCE_ID).param(resourceId)
                .and().eqWrap(SecurityRolePermission.FIELDS.ROLE_ID).param(roleId);
        return SecurityRolePermission.builder().build().find(Where.create(cond));
    }

    @Override
    public List<SecurityRolePermission> createAll(List<SecurityRolePermission> list) throws Exception {
        return JDBC.get().openSession(session -> session.insert(list));
    }

    @Override
    public void deleteByClientAndRoleIdAndResourceId(String client, String resourceId, String roleId) throws Exception{
        SecurityRolePermission.builder().client(client).resourceId(resourceId).roleId(roleId).build().delete();
    }
}
