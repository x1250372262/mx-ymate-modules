package com.mx.ymate.dev.support.jdbc.dao;

import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.IDBLocker;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

import java.util.List;


public interface IMxDao<Entity extends BaseEntity<Entity, String>> {

    Entity findById(String id, IDBLocker dbLocker, String... fields) throws Exception;

    Entity findById(String id, String... fields) throws Exception;

    Entity findFirst(Cond cond, IDBLocker dbLocker, String... fields) throws Exception;

    Entity findFirst(Cond cond, String... fields) throws Exception;

    Entity findByVar(String var,String value,String...fields) throws Exception;

    Entity findByVarNotId(String var,String value,String id,String...fields) throws Exception;

    IResultSet<Entity> find(Where where, IDBLocker dbLocker, Page page, String... fields) throws Exception;

    IResultSet<Entity> find(Where where, Page page, String... fields) throws Exception;

    IResultSet<Entity> find(Where where, String... fields) throws Exception;

    IResultSet<Entity> find(Cond cond, String... fields) throws Exception;

    IResultSet<Entity> find(Cond cond, Page page, String... fields) throws Exception;

    IResultSet<Entity> find(String... fields) throws Exception;

    Entity create(Entity entity) throws Exception;

    Entity update(Entity entity, String... fields) throws Exception;

    int delete(String id) throws Exception;

    int delete(Cond cond) throws Exception;

    Entity delete(Entity entity) throws Exception;

    int[] delete(String[] ids) throws Exception;

    List<Entity> delete(List<Entity> entities) throws Exception;

}
