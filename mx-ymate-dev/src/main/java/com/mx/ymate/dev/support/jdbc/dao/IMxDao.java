package com.mx.ymate.dev.support.jdbc.dao;

import net.ymate.platform.core.persistence.IResultSet;
import net.ymate.platform.core.persistence.Page;
import net.ymate.platform.persistence.jdbc.IDBLocker;
import net.ymate.platform.persistence.jdbc.query.Cond;
import net.ymate.platform.persistence.jdbc.query.Where;
import net.ymate.platform.persistence.jdbc.support.BaseEntity;

import java.util.List;

/**
 * @Author: mengxiang.
 * @create: 2023-08-03 16:58
 * @Description: 便捷查询dao接口
 */
public interface IMxDao<MxEntity extends BaseEntity<MxEntity, String>> {

    /**
     * 根据id查询
     * @param id
     * @param dbLocker
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findById(String id, IDBLocker dbLocker, String... fields) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findById(String id, String... fields) throws Exception;

    /**
     * 查询一条数据
     * @param cond
     * @param dbLocker
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findFirst(Cond cond, IDBLocker dbLocker, String... fields) throws Exception;

    /**
     * 查询一条数据
     * @param cond
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findFirst(Cond cond, String... fields) throws Exception;

    /**
     * 查询一条数据
     * @param where
     * @param dbLocker
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findFirst(Where where, IDBLocker dbLocker, String... fields) throws Exception;

    /**
     * 查询一条数据
     * @param where
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findFirst(Where where, String... fields) throws Exception;

    /**
     * 根据某个字段查询
     * @param var
     * @param value
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findByVar(String var, Object value, String... fields) throws Exception;

    /**
     * 根据某个字段查询 排除掉自己
     * @param var
     * @param value
     * @param id
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity findByVarNotId(String var, Object value, String id, String... fields) throws Exception;

    /**
     * 查询所有
     * @param where
     * @param dbLocker
     * @param page
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Where where, IDBLocker dbLocker, Page page, String... fields) throws Exception;

    /**
     * 查询所有
     * @param where
     * @param page
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Where where, Page page, String... fields) throws Exception;

    /**
     * 查询所有
     * @param where
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Where where, String... fields) throws Exception;

    /**
     * 查询所有
     * @param cond
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Cond cond, String... fields) throws Exception;

    /**
     * 查询所有
     * @param cond
     * @param page
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Cond cond, Page page, String... fields) throws Exception;

    /**
     * 查询所有
     * @param page
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(Page page, String... fields) throws Exception;

    /**
     * 查询所有
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> find(String... fields) throws Exception;

    /**
     * 根据某个字段查询所有
     * @param var
     * @param value
     * @param fields
     * @return
     * @throws Exception
     */
    IResultSet<MxEntity> findAllByVar(String var, Object value, String... fields) throws Exception;

    /**
     * 添加
     * @param mxEntity
     * @return
     * @throws Exception
     */
    MxEntity create(MxEntity mxEntity) throws Exception;

    /**
     * 批量添加
     * @param entityList
     * @return
     * @throws Exception
     */
    List<MxEntity> createAll(List<MxEntity> entityList) throws Exception;

    /**
     * 修改
     * @param mxEntity
     * @param fields
     * @return
     * @throws Exception
     */
    MxEntity update(MxEntity mxEntity, String... fields) throws Exception;

    /**
     * 批量修改
     * @param entityList
     * @param fields
     * @return
     * @throws Exception
     */
    List<MxEntity> updateAll(List<MxEntity> entityList, String... fields) throws Exception;

    /**
     * 删除id删除
     * @param id
     * @return
     * @throws Exception
     */
    int delete(String id) throws Exception;

    /**
     * 根据条件删除
     * @param cond
     * @return
     * @throws Exception
     */
    int delete(Cond cond) throws Exception;

    /**
     * 根据实体类删除
     * @param mxEntity
     * @return
     * @throws Exception
     */
    MxEntity delete(MxEntity mxEntity) throws Exception;

    /**
     * 批量根据id删除
     * @param ids
     * @return
     * @throws Exception
     */
    int[] delete(String[] ids) throws Exception;

    /**
     * 批量删除
     * @param mxEntityList
     * @return
     * @throws Exception
     */
    List<MxEntity> delete(List<MxEntity> mxEntityList) throws Exception;

}
