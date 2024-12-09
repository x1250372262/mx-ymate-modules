package com.mx.ymate.security.adapter.impl;

import cn.dev33.satoken.SaManager;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.adapter.ICacheStorageAdapter;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.satoken.cache.RedisDao;
import com.mx.ymate.security.satoken.cache.RedisStp;
import net.ymate.platform.commons.json.JsonWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:28.
 * @Description:
 */
public class RedisCacheStorageAdapter implements ICacheStorageAdapter {
    @Override
    public void init() {
        SaManager.setSaTokenDao(new RedisDao());
        SaManager.setStpInterface(new RedisStp());
    }

    @Override
    public void cacheUser(String userKey, LoginUser loginUser) throws Exception {
        RedisApi.strDelete(userKey);
        RedisApi.strSet(userKey, JsonWrapper.toJsonString(loginUser));
    }

    @Override
    public LoginUser getUser(String userKey) throws Exception {
        return JsonWrapper.deserialize(RedisApi.strGet(userKey), LoginUser.class);
    }

    @Override
    public void clearUser(String userKey) throws Exception {
        RedisApi.strDelete(userKey);
    }

    @Override
    public void lock(String lockKey, String loginId) throws Exception {
        RedisApi.strSet(loginId, loginId);
    }

    @Override
    public void unlock(String lockKey) throws Exception {
        RedisApi.strDelete(lockKey);
    }

    @Override
    public boolean checkLock(String lockKey) throws Exception {
        String data = RedisApi.strGet(lockKey);
        return StringUtils.isNotBlank(data);
    }

    @Override
    public List<String> permissionList(String permissionKey) throws Exception {
        List<String> permissionList = new ArrayList<>();
        String permissionStr = RedisApi.strGet(permissionKey);
        if (StringUtils.isNotBlank(permissionStr)) {
            JSONArray redisPermissionList = JSONObject.parseArray(permissionStr);
            for (Object redisRole : redisPermissionList) {
                permissionList.add(Convert.toStr(redisRole));
            }
        }
        return permissionList;
    }

    @Override
    public void cachePermission(String permissionKey, List<String> permissionList) throws Exception {
        RedisApi.strDelete(permissionKey);
        RedisApi.strSet(permissionKey, JSONObject.toJSONString(permissionList));
    }

    @Override
    public void clearPermission(String permissionKey) throws Exception {
        RedisApi.strDelete(permissionKey);
    }
}
