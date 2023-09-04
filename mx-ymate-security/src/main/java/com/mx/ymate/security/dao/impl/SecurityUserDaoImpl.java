package com.mx.ymate.security.dao.impl;

import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.security.base.model.SecurityRole;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.base.model.SecurityUserRole;
import com.mx.ymate.security.base.vo.SecurityUserListVO;
import com.mx.ymate.security.dao.ISecurityUserDao;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.*;

/**
 * @Author: mengxiang.
 * @create: 2022-07-04 11:32
 * @Description:
 */
@Bean
public class SecurityUserDaoImpl implements ISecurityUserDao {
    @Override
    public SecurityUser findById(String id, String... fields) throws Exception {
        return SecurityUser.builder().id(id).build().load(Fields.create(fields));
    }

    @Override
    public SecurityUser findByUserNameAndClient(String userName, String client) throws Exception {
        return SecurityUser.builder().userName(userName).client(client).build().findFirst();
    }

    @Override
    public SecurityUser update(SecurityUser securityUser, String... fields) throws Exception {
        return securityUser.update(Fields.create(fields));
    }

    @Override
    public SecurityUser create(SecurityUser securityUser) throws Exception {
        return securityUser.save();
    }

    @Override
    public IResultSet<SecurityUserListVO> findAll(String userName, String realName, Integer disableStatus, String client, String resourceId, Page page) throws Exception {
        Cond cond = Cond.create().eqWrap("su", SecurityUser.FIELDS.FOUNDER).param(Constants.BOOL_FALSE)
                .and().eqWrap("su", SecurityUser.FIELDS.CLIENT).param(client)
                .and().eqWrap("su", SecurityUser.FIELDS.RESOURCE_ID).param(resourceId)
                .exprNotEmpty(userName, c -> c.and().likeWrap("su", SecurityUser.FIELDS.USER_NAME).param("%" + userName + "%"))
                .exprNotEmpty(realName, c -> c.and().likeWrap("su", SecurityUser.FIELDS.REAL_NAME).param("%" + realName + "%"))
                .exprNotEmpty(disableStatus, c -> c.and().eqWrap("su", SecurityUser.FIELDS.DISABLE_STATUS).param(disableStatus));
        return JDBC.get().openSession(session -> {
            String prefix = session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
            Join surJoin = Join.left(prefix, SecurityUserRole.TABLE_NAME).alias("sur")
                    .on(Cond.create().opt(Fields.field("sur", SecurityUserRole.FIELDS.USER_ID), Cond.OPT.EQ, Fields.field("su", SecurityUser.FIELDS.ID)));
            Join srJoin = Join.left(prefix, SecurityRole.TABLE_NAME).alias("sr")
                    .on(Cond.create().opt(Fields.field("sr", SecurityRole.FIELDS.ID), Cond.OPT.EQ, Fields.field("sur", SecurityUserRole.FIELDS.ROLE_ID)));
            Select select = Select.create(prefix, SecurityUser.TABLE_NAME, "su")
                    .join(surJoin).join(srJoin)
                    .field("su", SecurityUser.FIELDS.ID)
                    .field("su", SecurityUser.FIELDS.USER_NAME)
                    .field("su", SecurityUser.FIELDS.REAL_NAME)
                    .field("su", SecurityUser.FIELDS.PHOTO_URI)
                    .field("su", SecurityUser.FIELDS.CREATE_TIME)
                    .field("su", SecurityUser.FIELDS.LAST_MODIFY_TIME)
                    .field("su", SecurityUser.FIELDS.DISABLE_STATUS)
                    .field("su", SecurityUser.FIELDS.LOGIN_LOCK_STATUS)
                    .field("su", SecurityUser.FIELDS.GENDER)
                    .field(Func.aggregate.GROUP_CONCAT(Fields.field("sr", SecurityRole.FIELDS.NAME)), "role_name")
                    .where(cond).groupBy("su",SecurityUser.FIELDS.ID).orderByDesc("su",SecurityUser.FIELDS.CREATE_TIME);
            return session.find(SQL.create(select), new BeanResultSetHandler<>(SecurityUserListVO.class), page);
        });
    }

    @Override
    public SecurityUser findByUserNameAndClientAndResourceId(String userName, String client, String resourceId) throws Exception {
        Cond cond = Cond.create().eqWrap(SecurityUser.FIELDS.USER_NAME).param(userName)
                .and().eqWrap(SecurityUser.FIELDS.RESOURCE_ID).param(resourceId)
                .and().eqWrap(SecurityUser.FIELDS.CLIENT).param(client);
        return SecurityUser.builder().build().findFirst(Where.create(cond));
    }
}
