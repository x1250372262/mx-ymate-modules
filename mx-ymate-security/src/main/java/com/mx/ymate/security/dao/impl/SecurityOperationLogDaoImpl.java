package com.mx.ymate.security.dao.impl;

import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.dao.ISecurityOperationLogDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 11:58
 * @Description:
 */
@Bean
public class SecurityOperationLogDaoImpl implements ISecurityOperationLogDao {
    @Override
    public SecurityOperationLog create(SecurityOperationLog securityOperationLog) throws Exception {
        return securityOperationLog.save();
    }

    @Override
    public IResultSet<SecurityOperationLog> findAll(String resourceId, String client, String title, Long startTime, Long endTime, Page page) throws Exception {
        Cond cond = Cond.create()
                .eqWrap(SecurityOperationLog.FIELDS.RESOURCE_ID).param(resourceId)
                .and().eqWrap(SecurityOperationLog.FIELDS.CLIENT).param(client)
                .exprNotEmpty(title, c -> c.and().likeWrap(SecurityOperationLog.FIELDS.TITLE).param("%" + title + "%"))
                .exprNotEmpty(startTime, c -> c.and().gtEqWrap(SecurityOperationLog.FIELDS.CREATE_TIME).param(startTime))
                .exprNotEmpty(endTime, c -> c.and().ltEqWrap(SecurityOperationLog.FIELDS.CREATE_TIME).param(endTime));
        return SecurityOperationLog.builder().build().find(Where.create(cond).orderByDesc(SecurityOperationLog.FIELDS.CREATE_TIME), page);
    }

    @Override
    public SecurityOperationLog findById(String id, String... fields) throws Exception {
        return SecurityOperationLog.builder().id(id).build().load(Fields.create(fields));
    }

    @Override
    public int[] deleteByIds(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(SecurityOperationLog.class, ids));
    }
}
