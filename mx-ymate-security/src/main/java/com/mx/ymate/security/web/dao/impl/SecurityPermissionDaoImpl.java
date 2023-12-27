package com.mx.ymate.security.web.dao.impl;

import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:12
 * @Description:
 */
@Bean
public class SecurityPermissionDaoImpl implements ISecurityPermissionDao {
    @Override
    public IResultSet<SecurityPermission> findAll(String client, String resourceId) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityPermission.FIELDS.CLIENT).param(client)
                .and().eqWrap(SecurityPermission.FIELDS.RESOURCE_ID).param(resourceId);
        return SecurityPermission.builder().build().find(Where.create(cond));
    }
}
