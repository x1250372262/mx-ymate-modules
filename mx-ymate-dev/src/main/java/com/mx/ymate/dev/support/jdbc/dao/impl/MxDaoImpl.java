package com.mx.ymate.dev.support.jdbc.dao.impl;

import com.mx.ymate.dev.support.jdbc.dao.IMxDao;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.persistence.Fields;
import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.core.persistence.annotation.Entity;
import net.ymate.platform.persistence.jdbc.IDBLocker;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.query.*;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

import java.util.List;

@Bean
public class MxDaoImpl<MxEntity extends BaseEntity<MxEntity, String>> implements IMxDao<MxEntity> {


    private final Class<MxEntity> entityClass;

    @SuppressWarnings("unchecked")
    public MxDaoImpl() {
        entityClass = (Class<MxEntity>) getEntityClass(getClass());
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            throw new RuntimeException("Dao最多支持两层继承");
        }
    }

    private static Class<?> getEntityClass(Class<?> clazz) {
        final String name = clazz.getName();
        if (name.contains("$$EnhancerBy") || name.contains("_$$_")) {
            Class<?> resultClass = ClassUtils.getParameterizedTypes(clazz.getSuperclass()).get(0);
            Entity entity = resultClass.getAnnotation(Entity.class);
            if (entity != null) {
                return resultClass;
            }
            Bean bean = resultClass.getAnnotation(Bean.class);
            if (bean == null) {
                throw new RuntimeException("Dao异常，请检查代码");
            }
            return ClassUtils.getParameterizedTypes(resultClass).get(0);
        }
        return clazz;
    }

    @Override
    public MxEntity findById(String id, IDBLocker dbLocker, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<MxEntity> entitySql = EntitySQL.create(entityClass);
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
    public MxEntity findById(String id, String... fields) throws Exception {
        return findById(id, null, fields);
    }

    @Override
    public MxEntity findFirst(Cond cond, IDBLocker dbLocker, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<MxEntity> entitySql = EntitySQL.create(entityClass);
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
    public MxEntity findFirst(Cond cond, String... fields) throws Exception {
        return findFirst(cond, null, fields);
    }

    @Override
    public MxEntity findByVar(String var, String value, String... fields) throws Exception {
        return findFirst(Cond.create().eqWrap(var).param(value), fields);
    }

    @Override
    public MxEntity findByVarNotId(String var, String value, String id, String... fields) throws Exception {
        return findFirst(Cond.create().eqWrap(var).param(value).and().notEqWrap("id").param(id), fields);
    }

    @Override
    public IResultSet<MxEntity> find(Where where, IDBLocker dbLocker, Page page, String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<MxEntity> entitySql = EntitySQL.create(entityClass);
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
    public IResultSet<MxEntity> find(Where where, Page page, String... fields) throws Exception {
        return find(where, null, page, fields);
    }

    @Override
    public IResultSet<MxEntity> find(Where where, String... fields) throws Exception {
        return find(where, null, null, fields);
    }

    @Override
    public IResultSet<MxEntity> find(Cond cond, String... fields) throws Exception {
        return find(cond, null, fields);
    }

    @Override
    public IResultSet<MxEntity> find(Cond cond, Page page, String... fields) throws Exception {
        return find(Where.create(cond), null, page, fields);
    }

    @Override
    public IResultSet<MxEntity> find(String... fields) throws Exception {
        return JDBC.get().openSession(session -> {
            EntitySQL<MxEntity> entitySql = EntitySQL.create(entityClass);
            if (fields != null && fields.length > 0) {
                entitySql.field(Fields.create(fields));
            }
            return session.find(entitySql);
        });
    }

    @Override
    public MxEntity create(MxEntity entity) throws Exception {
        return JDBC.get().openSession(session -> session.insert(entity));
    }

    @Override
    public MxEntity update(MxEntity entity, String... fields) throws Exception {
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
    public MxEntity delete(MxEntity entity) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entity));
    }

    @Override
    public int[] delete(String[] ids) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entityClass, ids));
    }

    @Override
    public List<MxEntity> delete(List<MxEntity> entities) throws Exception {
        return JDBC.get().openSession(session -> session.delete(entities));
    }
}
