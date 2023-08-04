package com.mx.ymate.dev.support.jdbc.dao;

import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.IDBLocker;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

import java.util.List;


public interface IMxDao<MxEntity extends BaseEntity<MxEntity, String>> {

    MxEntity findById(String id, IDBLocker dbLocker, String... fields) throws Exception;

    MxEntity findById(String id, String... fields) throws Exception;

    MxEntity findFirst(Cond cond, IDBLocker dbLocker, String... fields) throws Exception;

    MxEntity findFirst(Cond cond, String... fields) throws Exception;

    MxEntity findByVar(String var, String value, String... fields) throws Exception;

    MxEntity findByVarNotId(String var, String value, String id, String... fields) throws Exception;

    IResultSet<MxEntity> find(Where where, IDBLocker dbLocker, Page page, String... fields) throws Exception;

    IResultSet<MxEntity> find(Where where, Page page, String... fields) throws Exception;

    IResultSet<MxEntity> find(Where where, String... fields) throws Exception;

    IResultSet<MxEntity> find(Cond cond, String... fields) throws Exception;

    IResultSet<MxEntity> find(Cond cond, Page page, String... fields) throws Exception;

    IResultSet<MxEntity> find(String... fields) throws Exception;

    MxEntity create(MxEntity mxEntity) throws Exception;

    List<MxEntity> createAll(List<MxEntity> entityList) throws Exception;

    MxEntity update(MxEntity mxEntity, String... fields) throws Exception;

    List<MxEntity> updateAll(List<MxEntity> entityList, String... fields) throws Exception;

    int delete(String id) throws Exception;

    int delete(Cond cond) throws Exception;

    MxEntity delete(MxEntity mxEntity) throws Exception;

    int[] delete(String[] ids) throws Exception;

    List<MxEntity> delete(List<MxEntity> mxEntityList) throws Exception;

}
