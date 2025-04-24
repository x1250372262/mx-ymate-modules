package com.mx.ymate.security.web.dao.impl;

import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.web.dao.ISecurityPermissionDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityPermissionDaoImpl implements ISecurityPermissionDao {
    @Override
    public IResultSet<SecurityPermission> findAll(String client) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityPermission.FIELDS.CLIENT).param(client);
        return SecurityPermission.builder().build().find(Where.create(cond));
    }
}
