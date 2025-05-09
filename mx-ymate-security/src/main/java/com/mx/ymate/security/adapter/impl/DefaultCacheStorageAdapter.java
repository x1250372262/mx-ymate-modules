package com.mx.ymate.security.adapter.impl;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.convert.Convert;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mx.ymate.security.adapter.ICacheStorageAdapter;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.satoken.cache.DefaultDao;
import com.mx.ymate.security.satoken.cache.DefaultStp;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:28.
 * @Description:
 */
public class DefaultCacheStorageAdapter implements ICacheStorageAdapter {

    /**
     * 存储数据的集合
     */
    public Cache<String, Object> dataMap = Caffeine.newBuilder().build();


    @Override
    public void init() {
        SaManager.setSaTokenDao(new DefaultDao());
        SaManager.setStpInterface(new DefaultStp());
    }

    @Override
    public void cacheUser(String userKey, LoginUser loginUser) throws Exception {
        dataMap.invalidate(userKey);
        dataMap.put(userKey, loginUser);
    }

    @Override
    public LoginUser getUser(String userKey) throws Exception {
        Object data = dataMap.getIfPresent(userKey);
        return data != null ? (LoginUser) data : null;
    }

    @Override
    public void clearUser(String userKey) throws Exception {
        dataMap.invalidate(userKey);
    }

    @Override
    public void lock(String lockKey, String loginId) throws Exception {
        dataMap.put(lockKey, loginId);
    }

    @Override
    public void unlock(String lockKey) throws Exception {
        dataMap.invalidate(lockKey);
    }

    @Override
    public boolean checkLock(String lockKey) throws Exception {
        Object data = dataMap.getIfPresent(lockKey);
        return data != null;
    }

    @Override
    public List<String> permissionList(String permissionKey) throws Exception {
        Object permissionObj = dataMap.getIfPresent(permissionKey);
        if (permissionObj == null) {
            return new ArrayList<>();
        }
        return Convert.toList(String.class, permissionObj);
    }

    @Override
    public void cachePermission(String permissionKey, List<String> permissionList) throws Exception {
        dataMap.invalidate(permissionKey);
        dataMap.put(permissionKey, permissionList);
    }

    @Override
    public void clearPermission(String permissionKey) throws Exception {
        dataMap.invalidate(permissionKey);
    }
}
