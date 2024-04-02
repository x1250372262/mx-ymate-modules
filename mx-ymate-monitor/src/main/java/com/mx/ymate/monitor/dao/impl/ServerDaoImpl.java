package com.mx.ymate.monitor.dao.impl;

import com.mx.ymate.dev.support.jdbc.MxJdbc;
import com.mx.ymate.dev.support.jdbc.dao.impl.MxDaoImpl;
import com.mx.ymate.dev.support.page.PageBean;
import com.mx.ymate.monitor.dao.IServerDao;
import com.mx.ymate.monitor.model.Server;
import com.mx.ymate.monitor.vo.ServerListVO;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.sql.SecurityJdbc;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.impl.BeanResultSetHandler;
import net.ymate.platform.persistence.jdbc.query.*;

/**
 * @Author: xujianpeng.
 * @Create: 2024/2/23 11:51
 * @Description:
 */
@Bean
public class ServerDaoImpl extends MxDaoImpl<Server> implements IServerDao {
    @Override
    public IResultSet<ServerListVO> findAll(String name, String ip, PageBean pageBean) throws Exception {
        Cond cond = Cond.create().eqOne()
                .exprNotEmpty(name, c -> c.and().likeWrap("s", Server.FIELDS.NAME).param(Like.create(name).contains()))
                .exprNotEmpty(ip, c -> c.and().likeWrap("s", Server.FIELDS.IP).param(Like.create(ip).contains()));
        return JDBC.get().openSession(session -> {
            String prefix = MxJdbc.tablePrefix(session);
            Join join = SecurityJdbc.securityJoin(prefix, "s", Server.FIELDS.LAST_MODIFY_USER);
            Select select = Select.create(prefix, Server.TABLE_NAME, "s")
                    .join(join)
                    .field("s", Server.FIELDS.ID)
                    .field("s", Server.FIELDS.NAME)
                    .field("s", Server.FIELDS.IP)
                    .field("s", Server.FIELDS.USER)
                    .field("s", Server.FIELDS.PASSWORD)
                    .field("s", Server.FIELDS.REMARK)
                    .field("s", Server.FIELDS.LAST_MODIFY_TIME)
                    .field("su", SecurityUser.FIELDS.REAL_NAME, Server.FIELDS.LAST_MODIFY_USER)
                    .where(cond).orderByDesc("s", Server.FIELDS.CREATE_TIME);
            return session.find(SQL.create(select), new BeanResultSetHandler<>(ServerListVO.class), pageBean.toPage());
        });
    }
}
