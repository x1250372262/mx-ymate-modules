package com.mx.ymate.security.adapter;

import com.mx.ymate.security.base.bean.LoginUser;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:18.
 * @Description:
 */
public interface ICacheStorageAdapter {

    /**
     * 初始化数据
     */
    void init();

    /**
     * 缓存用户
     *
     * @param userKey
     * @param loginUser
     * @throws Exception
     */
    void cacheUser(String userKey, LoginUser loginUser) throws Exception;

    /**
     * 获取用户
     *
     * @param userKey
     * @return
     * @throws Exception
     */
    LoginUser getUser(String userKey) throws Exception;

    /**
     * 清空用户缓存
     * @param userKey
     * @throws Exception
     */
    void clearUser(String userKey) throws Exception;

    /**
     * 锁定
     *
     * @param lockKey
     * @param loginId
     * @throws Exception
     */
    void lock(String lockKey, String loginId) throws Exception;

    /**
     * 解锁
     *
     * @param lockKey
     * @throws Exception
     */
    void unlock(String lockKey) throws Exception;

    /**
     * 检查锁定状态
     *
     * @param lockKey
     * @return
     * @throws Exception
     */
    boolean checkLock(String lockKey) throws Exception;

    /**
     * 权限列表
     *
     * @param permissionKey
     * @return
     * @throws Exception
     */
    List<String> permissionList(String permissionKey) throws Exception;

    /**
     * 缓存权限
     *
     * @param permissionKey
     * @param permissionList
     * @throws Exception
     */
    void cachePermission(String permissionKey, List<String> permissionList) throws Exception;

    /**
     * 清空缓存权限
     * @param permissionKey
     * @throws Exception
     */
    void clearPermission(String permissionKey) throws Exception;
}