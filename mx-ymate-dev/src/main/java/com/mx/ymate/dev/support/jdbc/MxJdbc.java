package com.mx.ymate.dev.support.jdbc;

import com.mx.ymate.dev.constants.Constants;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.persistence.jdbc.IDatabaseSession;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Join;

public class MxJdbc {

    public final static String DELETE_STATUS = "delete_status";

    public static String tablePrefix(IDatabaseSession session) {
        return session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
    }

    public static Cond eqOne() {
        return Cond.create().eqOne();
    }

    public static Cond deleteStatus(){
        return deleteStatus(null);
    }

    public static Cond deleteStatus(String alias){
        return Cond.create().eqWrap(alias,DELETE_STATUS).param(Constants.BOOL_FALSE);
    }



    public static Join innerJoin(String prefix, String joinTableName, String joinTableAlias, String joinFieldName, String tableAlias, String fieldName) {
        return Join.inner(prefix, joinTableName).alias(joinTableAlias)
                .on(Cond.create().opt(Fields.field(joinTableAlias, joinFieldName), Cond.OPT.EQ, Fields.field(tableAlias, fieldName)));
    }

    public static Join leftJoin(String prefix, String joinTableName, String joinTableAlias, String joinFieldName, String tableAlias, String fieldName) {
        return Join.left(prefix, joinTableName).alias(joinTableAlias)
                .on(Cond.create().opt(Fields.field(joinTableAlias, joinFieldName), Cond.OPT.EQ, Fields.field(tableAlias, fieldName)));
    }

    public static Join rightJoin(String prefix, String joinTableName, String joinTableAlias, String joinFieldName, String tableAlias, String fieldName) {
        return Join.right(prefix, joinTableName).alias(joinTableAlias)
                .on(Cond.create().opt(Fields.field(joinTableAlias, joinFieldName), Cond.OPT.EQ, Fields.field(tableAlias, fieldName)));
    }
}
