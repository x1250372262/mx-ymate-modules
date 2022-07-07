package com.mx.ymate.security.dao.impl;

import com.mx.ymate.security.base.model.SecurityMenuRole;
import com.mx.ymate.security.base.model.SecurityRole;
import com.mx.ymate.security.base.vo.SecurityMenuRoleVO;
import com.mx.ymate.security.dao.ISecurityMenuRoleDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.IDatabaseSessionExecutor;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Join;
import net.ymate.platform.persistence.jdbc.query.SQL;
import net.ymate.platform.persistence.jdbc.query.Select;

/**
 * @Author: 徐建鹏.
 * @create: 2022-07-04 13:37
 * @Description:
 */
@Bean
public class SecurityMenuRoleDaoImpl implements ISecurityMenuRoleDao {
    @Override
    public IResultSet<SecurityMenuRoleVO> findAll(String menuId, String resourceId, String name, Page page) throws Exception {
        Cond cond = Cond.create()
                .eqWrap("smr", SecurityMenuRole.FIELDS.MENU_ID).param(menuId)
                .and().eqWrap("smr", SecurityMenuRole.FIELDS.RESOURCE_ID).param(resourceId)
                .exprNotEmpty(name, c -> c.and().likeWrap("mr", SecurityRole.FIELDS.NAME).param("%" + name + "%"));
        return JDBC.get().openSession(session -> {
            String prefix = session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
            Join join = Join.inner(prefix, SecurityRole.TABLE_NAME).alias("mr")
                    .on(Cond.create().opt(Fields.field("smr", SecurityMenuRole.FIELDS.ROLE_ID), Cond.OPT.EQ, Fields.field("mr", SecurityRole.FIELDS.ID)));
            Select select = Select.create(prefix, SecurityMenuRole.TABLE_NAME, "smr")
                    .join(join)
                    .field("smr", SecurityMenuRole.FIELDS.ID)
                    .field("smr", SecurityMenuRole.FIELDS.CREATE_TIME)
                    .field("mr", SecurityRole.FIELDS.NAME)
                    .where(cond).orderByDesc(Fields.field("mr", SecurityRole.FIELDS.CREATE_TIME));
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityMenuRoleVO.class), page);
        });
    }

    @Override
    public SecurityMenuRole findById(String id, String... fields) throws Exception {
        return SecurityMenuRole.builder().id(id).build().load(Fields.create(fields));
    }

    @Override
    public SecurityMenuRole findByMenuIdAndRoleIdAndClient(String menuId, String roleId, String client) throws Exception {
        return SecurityMenuRole.builder().menuId(menuId).roleId(roleId).client(client).build().findFirst();
    }

    @Override
    public SecurityMenuRole create(SecurityMenuRole securityMenuRole) throws Exception {
        return securityMenuRole.save();
    }

    @Override
    public int[] deleteByIds(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(SecurityMenuRole.class, ids));
    }
}
