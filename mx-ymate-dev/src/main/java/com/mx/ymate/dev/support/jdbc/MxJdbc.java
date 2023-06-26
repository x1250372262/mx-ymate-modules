package com.mx.ymate.dev.support.jdbc;

import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.persistence.jdbc.IDatabaseSession;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Join;

public class MxJdbc {

    public static String tablePrefix(IDatabaseSession session) {
        return session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
    }

    public static Cond eqOne() {
        return Cond.create().eqOne();
    }

    public static String likeParam(Object param) {
        return likeParam(param, true, true);
    }

    public static String likeParamBefore(Object param) {
        return likeParam(param, true, false);
    }

    public static String likeParamAfter(Object param) {
        return likeParam(param, false, true);
    }

    private static String likeParam(Object param, boolean before, boolean after) {
        if (before && !after) {
            return "%" + param;
        } else if (!before && after) {
            return param + "%";
        } else {
            return "%" + param + "%";
        }
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
