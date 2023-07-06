package com.mx.ymate.dev.support.jdbc.dao.impl;

import com.mx.ymate.dev.support.jdbc.dao.IMxDao;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.IDBLocker;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.query.*;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

import java.util.List;

@Bean
public class MxDaoImpl<Entity extends BaseEntity<Entity, String>> implements IMxDao<Entity> {


    private final Class<Entity> entityClass;

    @SuppressWarnings("unchecked")
    public MxDaoImpl() {
        entityClass = (Class<Entity>) ClassUtils.getParameterizedTypes(getUsefulClass(getClass())).get(0);
    }

    private static Class<?> getUsefulClass(Class<?> clazz) {
        final String name = clazz.getName();
        if (name.contains("$$EnhancerBy") || name.contains("_$$_")) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    @Override
    public Entity findById(String id, IDBLocker dbLocker, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<Entity> entitySql = EntitySQL.create(entityClass);
            if (fields != null && fields.length > 0) {
                entitySql.field(Fields.create(fields));
            }
            if (dbLocker != null) {
                entitySql.forUpdate(dbLocker);
            }
            return session.find(entitySql, id);
        });
    }

    @Override
    public Entity findById(String id, String... fields) throws Exception {
        return findById(id, null, fields);
    }

    @Override
    public Entity findFirst(Cond cond, IDBLocker dbLocker, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<Entity> entitySql = EntitySQL.create(entityClass);
            if (fields != null && fields.length > 0) {
                entitySql.field(Fields.create(fields));
            }
            if (dbLocker != null) {
                entitySql.forUpdate(dbLocker);
            }
            return session.findFirst(entitySql, Where.create(cond));
        });
    }

    @Override
    public Entity findFirst(Cond cond, String... fields) throws Exception {
        return findFirst(cond, null, fields);
    }

    @Override
    public Entity findByVar(String var, String value, String... fields) throws Exception {
        return findFirst(Cond.create().eqWrap(var).param(value),fields);
    }

    @Override
    public Entity findByVarNotId(String var, String value, String id, String... fields) throws Exception {
        return findFirst(Cond.create().eqWrap(var).param(value).and().notEqWrap("id").param(id),fields);
    }

    @Override
    public IResultSet<Entity> find(Where where, IDBLocker dbLocker, Page page, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<Entity> entitySql = EntitySQL.create(entityClass);
            if (fields != null && fields.length > 0) {
                entitySql.field(Fields.create(fields));
            }
            if (dbLocker != null) {
                entitySql.forUpdate(dbLocker);
            }
            return session.find(entitySql, where, page);
        });
    }

    @Override
    public IResultSet<Entity> find(Where where, Page page, String... fields) throws Exception {
        return find(where, null, page, fields);
    }

    @Override
    public IResultSet<Entity> find(Where where, String... fields) throws Exception {
        return find(where, null, null, fields);
    }

    @Override
    public IResultSet<Entity> find(Cond cond, String... fields) throws Exception {
        return find(cond, null, fields);
    }

    @Override
    public IResultSet<Entity> find(Cond cond, Page page, String... fields) throws Exception {
        return find(Where.create(cond), null, page, fields);
    }

    @Override
    public IResultSet<Entity> find(String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<Entity> entitySql = EntitySQL.create(entityClass);
            if (fields != null && fields.length > 0) {
                entitySql.field(Fields.create(fields));
            }
            return session.find(entitySql);
        });
    }

    @Override
    public Entity create(Entity entity) throws Exception {
        return JDBC.get().openSession(session -> session.insert(entity));
    }

    @Override
    public Entity update(Entity entity, String... fields) throws Exception {
        return JDBC.get().openSession(session -> session.update(entity, Fields.create(fields)));
    }

    @Override
    public int delete(String id) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entityClass, id));
    }

    @Override
    public int delete(Cond cond) throws Exception {
        return JDBC.get().openSession(session -> {
            Delete delete = Delete.create(entityClass).where(cond);
            return session.executeForUpdate(SQL.create(delete));
        });
    }

    @Override
    public int[] delete(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entityClass, ids));
    }

    @Override
    public List<Entity> delete(List<Entity> entities) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entities));
    }
}
