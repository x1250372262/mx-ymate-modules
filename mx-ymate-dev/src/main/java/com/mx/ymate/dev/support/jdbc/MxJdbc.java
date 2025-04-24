package com.mx.ymate.dev.support.jdbc;

import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.jdbc.cond.MxCond;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.persistence.jdbc.IDatabaseSession;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Join;
import net.ymate.platform.persistence.jdbc.query.Like;

import java.lang.reflect.Field;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: jdbc简化操作
 */
public class MxJdbc {

    public final static String DELETE_STATUS = "delete_status";

    public static String tablePrefix(IDatabaseSession session) {
        return session.getConnectionHolder().getDataSourceConfig().getTablePrefix();
    }

    public static Cond eqOne() {
        return Cond.create().eqOne();
    }

    public static Cond createByBean(Object obj, boolean eqOne) throws IllegalAccessException {
        Cond cond = Cond.create();
        if (eqOne) {
            cond.eqOne();
        }
        Class<?> clazz = obj.getClass();
        for (Field field : ClassUtils.getFields(clazz, false)) {
            MxCond mxCond = field.getAnnotation(MxCond.class);
            if (mxCond == null) {
                continue;
            }
            String prefix = mxCond.prefix();
            Cond.OPT opt = mxCond.opt();
            String tableField = mxCond.tableField();
            field.setAccessible(true);
            Object value = field.get(obj);
            Object oldValue = value;
            if (Cond.OPT.LIKE.equals(opt)) {
                value = Like.create(BlurObject.bind(value).toStringValue()).contains();
            }
            if (mxCond.checkEmpty()) {
                Object finalValue = value;
                cond.exprNotEmpty(oldValue, c -> c.and().optWrap(Fields.field(prefix, tableField), opt).param(finalValue));
            } else {
                cond.and().optWrap(Fields.field(prefix, tableField), opt).param(value);
            }
        }
        return cond;
    }

    public static Cond deleteStatus() {
        return deleteStatus(null);
    }

    public static Cond deleteStatus(String alias) {
        return Cond.create().eqWrap(alias, DELETE_STATUS).param(Constants.BOOL_FALSE);
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
