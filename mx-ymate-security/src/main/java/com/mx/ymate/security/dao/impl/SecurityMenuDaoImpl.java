package com.mx.ymate.security.dao.impl;

import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.security.base.model.SecurityMenu;
import com.mx.ymate.security.base.model.SecurityMenuRole;
import com.mx.ymate.security.base.model.SecurityUserRole;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.dao.ISecurityMenuDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Join;
import net.ymate.platform.persistence.jdbc.query.SQL;
import net.ymate.platform.persistence.jdbc.query.Select;

/**
 * @Author: 徐建鹏.
 * @create: 2022-07-04 13:05
 * @Description:
 */
@Bean
public class SecurityMenuDaoImpl implements ISecurityMenuDao {
    @Override
    public IResultSet<SecurityMenuNavVO> findAllByType(Integer type,Integer hideStatus, String client, String resourceId) throws Exception {
        Cond cond = Cond.create()
                .eqWrap(SecurityMenu.FIELDS.CLIENT).param(client)
                .and().eqWrap(SecurityMenu.FIELDS.RESOURCE_ID).param(resourceId)
                .exprNotEmpty(type, c -> c.and().eqWrap(SecurityMenu.FIELDS.TYPE).param(type))
                .exprNotEmpty(hideStatus, c -> c.and().eqWrap(SecurityMenu.FIELDS.HIDE_STATUS).param(hideStatus));
        return JDBC.get().openSession(session -> {
            Select select = Select.create(SecurityMenu.TABLE_NAME,"sm")
                    .field("sm",SecurityMenu.FIELDS.ID)
                    .field("sm",SecurityMenu.FIELDS.PARENT_ID, "pid")
                    .field("sm",SecurityMenu.FIELDS.NAME)
                    .field("sm",SecurityMenu.FIELDS.NAME, "value")
                    .field("sm",SecurityMenu.FIELDS.ICON)
                    .field("sm",SecurityMenu.FIELDS.PATH)
                    .field("sm",SecurityMenu.FIELDS.URL)
                    .field("sm",SecurityMenu.FIELDS.SORT)
                    .where(cond).orderByAsc(Fields.create(SecurityMenu.FIELDS.SORT));
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityMenuNavVO.class));
        });
    }

    @Override
    public IResultSet<SecurityMenuNavVO> findAll(String userId, String client, String resourceId,Integer hideStatus) throws Exception {
        Cond cond = Cond.create()
                .eqWrap("sur", SecurityUserRole.FIELDS.USER_ID).param(userId).and().eqWrap("sur",SecurityMenu.FIELDS.HIDE_STATUS).param(Constants.BOOL_FALSE)
                .and().eqWrap("sm", SecurityMenu.FIELDS.TYPE).param(Constants.BOOL_FALSE)
                .and().eqWrap("sm", SecurityMenu.FIELDS.CLIENT).param(client)
                .and().eqWrap("smr", SecurityMenuRole.FIELDS.RESOURCE_ID).param(resourceId)
                .exprNotEmpty(hideStatus,c->c.and().eqWrap("sm",SecurityMenu.FIELDS.HIDE_STATUS).param(hideStatus));
        return JDBC.get().openSession(session -> {
            String prefix = session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
            Join mrJoin = Join.left(prefix, SecurityMenuRole.TABLE_NAME).alias("smr")
                    .on(Cond.create().opt(Fields.field("sm", SecurityMenu.FIELDS.ID), Cond.OPT.EQ, Fields.field("smr", SecurityMenuRole.FIELDS.MENU_ID)));
            Join urJoin = Join.left(prefix, SecurityUserRole.TABLE_NAME).alias("sur")
                    .on(Cond.create().opt(Fields.field("sur", SecurityUserRole.FIELDS.ROLE_ID), Cond.OPT.EQ, Fields.field("smr", SecurityMenuRole.FIELDS.ROLE_ID)));
            Select select = Select.create(prefix, SecurityMenu.TABLE_NAME,"sm")
                    .join(mrJoin).join(urJoin)
                    .field("sm", SecurityMenu.FIELDS.ID)
                    .field("sm", SecurityMenu.FIELDS.PARENT_ID, "pid")
                    .field("sm", SecurityMenu.FIELDS.NAME)
                    .field("sm", SecurityMenu.FIELDS.NAME, "value")
                    .field("sm", SecurityMenu.FIELDS.ICON)
                    .field("sm", SecurityMenu.FIELDS.PATH)
                    .field("sm", SecurityMenu.FIELDS.URL)
                    .field("sm", SecurityMenu.FIELDS.SORT)
                    .where(cond).orderByAsc(Fields.field("sm", SecurityMenu.FIELDS.SORT));
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityMenuNavVO.class));
        });
    }

    @Override
    public SecurityMenu create(SecurityMenu securityMenu) throws Exception {
        return securityMenu.save();
    }

    @Override
    public SecurityMenu update(SecurityMenu securityMenu, String... fields) throws Exception {
        return securityMenu.update(Fields.create(fields));
    }

    @Override
    public SecurityMenu findById(String id, String... fields) throws Exception {
        return SecurityMenu.builder().id(id).build().load(Fields.create(fields));
    }

    @Override
    public SecurityMenu findByParentId(String parentId, String... fields) throws Exception {
        return SecurityMenu.builder().parentId(parentId).build().findFirst(Fields.create(fields));
    }

    @Override
    public SecurityMenu delete(String id) throws Exception {
        return SecurityMenu.builder().id(id).build().delete();
    }
}
