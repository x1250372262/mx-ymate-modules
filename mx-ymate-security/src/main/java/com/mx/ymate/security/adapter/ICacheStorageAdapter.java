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

    void cacheUser(String key, LoginUser loginUser) throws Exception;

    LoginUser getUser(String userKey) throws Exception;

    void lock(String lockKey,String loginId) throws Exception;

    void unlock(String lockKey) throws Exception;

    boolean checkLock(String lockKey) throws Exception;

    List<String> permissionList(String permissionKey) throws Exception;

    void cachePermission(String permissionKey, List<String> permissionList) throws Exception;
}