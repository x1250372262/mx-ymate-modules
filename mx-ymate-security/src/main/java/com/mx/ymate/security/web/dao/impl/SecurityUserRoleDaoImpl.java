package com.mx.ymate.security.web.dao.impl;

import com.mx.ymate.security.base.model.SecurityPermission;
import com.mx.ymate.security.base.model.SecurityRole;
import com.mx.ymate.security.base.model.SecurityRolePermission;
import com.mx.ymate.security.base.model.SecurityUserRole;
import com.mx.ymate.security.base.vo.SecurityUserPermissionVO;
import com.mx.ymate.security.base.vo.SecurityUserRoleVO;
import com.mx.ymate.security.web.dao.ISecurityUserRoleDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.core.persistence.Params;
import net.ymate.platform.persistence.jdbc.IDatabaseSessionExecutor;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.*;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityUserRoleDaoImpl implements ISecurityUserRoleDao {
    @Override
    public void deleteByRoleIds(List<String> roleIds) throws Exception {
        JDBC.get().openSession((IDatabaseSessionExecutor<Object>) session -> {
            Delete delete = Delete.create(SecurityUserRole.TABLE_NAME)
                    .where(Cond.create().in(SecurityUserRole.FIELDS.ROLE_ID, Params.create(roleIds)));
            return session.executeForUpdate(SQL.create(delete));
        });
    }

    @Override
    public SecurityUserRole create(SecurityUserRole securityUserRole) throws Exception {
        return securityUserRole.save();
    }

    @Override
    public int[] deleteByIds(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(SecurityUserRole.class, ids));
    }

    @Override
    public IResultSet<SecurityUserPermissionVO> permissionList(String userId, String client) throws Exception {
        Cond cond = Cond.create().eqWrap("sur", SecurityUserRole.FIELDS.USER_ID).param(userId)
                .and().eqWrap("sur", SecurityUserRole.FIELDS.CLIENT).param(client);
        return JDBC.get().openSession(session -> {
            String prefix = session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
            Join spJoin = Join.inner(prefix, SecurityPermission.TABLE_NAME).alias("sp")
                    .on(Cond.create().opt(Fields.field("srp", SecurityRolePermission.FIELDS.PERMISSON_ID), Cond.OPT.EQ, Fields.field("sp", SecurityPermission.FIELDS.ID)));
            Join surJoin = Join.inner(prefix, SecurityUserRole.TABLE_NAME).alias("sur")
                    .on(Cond.create().opt(Fields.field("sur", SecurityUserRole.FIELDS.ROLE_ID), Cond.OPT.EQ, Fields.field("srp", SecurityRolePermission.FIELDS.ROLE_ID)));
            Select select = Select.create(prefix, SecurityRolePermission.TABLE_NAME, "srp")
                    .join(spJoin)
                    .join(surJoin)
                    .field("sp", SecurityPermission.FIELDS.ID, "permission_id")
                    .field("sp", SecurityPermission.FIELDS.PERMISSION_CODE)
                    .field("sp", SecurityPermission.FIELDS.PERMISSION_NAME)
                    .field("sur", SecurityUserRole.FIELDS.ROLE_ID)
                    .where(cond);
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityUserPermissionVO.class));
        });
    }

    @Override
    public IResultSet<SecurityUserRoleVO> roleList(String userId, String client, Page page) throws Exception {
        Cond cond = Cond.create().eqWrap("sur", SecurityUserRole.FIELDS.USER_ID).param(userId)
                .and().eqWrap("sur", SecurityUserRole.FIELDS.CLIENT).param(client);
        return JDBC.get().openSession(session -> {
            String prefix = session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
            Join srJoin = Join.inner(prefix, SecurityRole.TABLE_NAME).alias("sr")
                    .on(Cond.create().opt(Fields.field("sur", SecurityUserRole.FIELDS.ROLE_ID), Cond.OPT.EQ, Fields.field("sr", SecurityRole.FIELDS.ID)));
            Select select = Select.create(prefix, SecurityUserRole.TABLE_NAME, "sur")
                    .join(srJoin)
                    .field("sur", SecurityUserRole.FIELDS.ID)
                    .field("sur", SecurityUserRole.FIELDS.USER_ID)
                    .field("sur", SecurityUserRole.FIELDS.ROLE_ID)
                    .field("sur", SecurityUserRole.FIELDS.CREATE_TIME)
                    .field("sr", SecurityRole.FIELDS.NAME, "role_name")
                    .where(cond);
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityUserRoleVO.class), page);
        });
    }

    @Override
    public SecurityUserRole findByUserIdAndRoleIdAndClient(String userId, String roleId, String client) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityUserRole.FIELDS.USER_ID).param(userId)
                .and().eqWrap(SecurityUserRole.FIELDS.ROLE_ID).param(roleId)
                .and().eqWrap(SecurityUserRole.FIELDS.CLIENT).param(client);
        return SecurityUserRole.builder().build().findFirst(Where.create(cond));
    }
}
