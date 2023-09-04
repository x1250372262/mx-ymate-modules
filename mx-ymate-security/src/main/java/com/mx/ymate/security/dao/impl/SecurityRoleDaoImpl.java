package com.mx.ymate.security.dao.impl;

import com.mx.ymate.security.base.model.SecurityRole;
import com.mx.ymate.security.dao.ISecurityRoleDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 14:22
 * @Description:
 */
@Bean
public class SecurityRoleDaoImpl implements ISecurityRoleDao {
    @Override
    public IResultSet<SecurityRole> findAll(String resourceId, String client, String name, Page page) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityRole.FIELDS.RESOURCE_ID).param(resourceId)
                .and().eqWrap(SecurityRole.FIELDS.CLIENT).param(client)
                .exprNotEmpty(name, c -> c.and().likeWrap(SecurityRole.FIELDS.NAME).param("%" + name + "%"));
        return SecurityRole.builder().build().find(Where.create(cond).orderByDesc(SecurityRole.FIELDS.CREATE_TIME), page);
    }

    @Override
    public SecurityRole findByClientAndResourceIdAndName(String resourceId, String client, String name) throws Exception {
        return SecurityRole.builder().resourceId(resourceId).client(client).name(name).build().findFirst();
    }

    @Override
    public SecurityRole findByClientAndResourceIdAndNameNotId(String id, String resourceId, String client, String name) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityRole.FIELDS.RESOURCE_ID).param(resourceId)
                .and().eqWrap(SecurityRole.FIELDS.CLIENT).param(client)
                .and().eqWrap(SecurityRole.FIELDS.NAME).param(name)
                .and().notEqWrap(SecurityRole.FIELDS.ID).param(id);
        return SecurityRole.builder().build().findFirst(Where.create(cond));
    }

    @Override
    public SecurityRole create(SecurityRole securityRole) throws Exception {
        return securityRole.save();
    }

    @Override
    public SecurityRole update(SecurityRole securityRole, String... fields) throws Exception {
        return securityRole.update(Fields.create(fields));
    }

    @Override
    public SecurityRole findById(String id, String... fields) throws Exception {
        return SecurityRole.builder().id(id).build().load(Fields.create(fields));
    }

    @Override
    public int[] deleteByIds(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(SecurityRole.class, ids));
    }
}
