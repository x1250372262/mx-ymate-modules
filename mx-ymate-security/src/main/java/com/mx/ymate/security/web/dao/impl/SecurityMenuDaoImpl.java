package com.mx.ymate.security.web.dao.impl;

import com.mx.ymate.security.base.model.SecurityMenu;
import com.mx.ymate.security.base.vo.SecurityMenuNavVO;
import com.mx.ymate.security.web.dao.ISecurityMenuDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.SQL;
import net.ymate.platform.persistence.jdbc.query.Select;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class SecurityMenuDaoImpl implements ISecurityMenuDao {
    @Override
    public IResultSet<SecurityMenuNavVO> findAll(Integer type, Integer hideStatus, String client) throws Exception {
        Cond cond = Cond.create()
                .eqWrap(SecurityMenu.FIELDS.CLIENT).param(client)
                .exprNotEmpty(type, c -> c.and().eqWrap(SecurityMenu.FIELDS.TYPE).param(type))
                .exprNotEmpty(hideStatus, c -> c.and().eqWrap(SecurityMenu.FIELDS.HIDE_STATUS).param(hideStatus));
        return JDBC.get().openSession(session -> {
            Select select = Select.create(SecurityMenu.TABLE_NAME, "sm")
                    .field("sm", SecurityMenu.FIELDS.ID)
                    .field("sm", SecurityMenu.FIELDS.PARENT_ID, "pid")
                    .field("sm", SecurityMenu.FIELDS.NAME)
                    .field("sm", SecurityMenu.FIELDS.NAME, "value")
                    .field("sm", SecurityMenu.FIELDS.I18N_KEY)
                    .field("sm", SecurityMenu.FIELDS.ICON)
                    .field("sm", SecurityMenu.FIELDS.PATH)
                    .field("sm", SecurityMenu.FIELDS.URL)
                    .field("sm", SecurityMenu.FIELDS.SORT)
                    .field("sm", SecurityMenu.FIELDS.TYPE)
                    .field("sm", SecurityMenu.FIELDS.PERMISSION)
                    .where(cond).orderByAsc(Fields.create(SecurityMenu.FIELDS.SORT));
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
    public int delete(String id) throws Exception {
        return SecurityMenu.builder().id(id).build().delete();
    }
}
