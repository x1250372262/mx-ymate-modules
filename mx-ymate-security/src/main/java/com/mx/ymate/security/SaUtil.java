package com.mx.ymate.security;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.security.adapter.ICacheStorageAdapter;
import com.mx.ymate.security.base.bean.LoginUser;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.mx.ymate.security.base.config.SecurityConstants.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SaUtil {

    private final static ISecurityConfig MX_SECURITY_CONFIG = Security.get().getConfig();
    private final static ICacheStorageAdapter CACHE_STORAGE_ADAPTER = MX_SECURITY_CONFIG.cacheStoreAdapter();

    public static boolean isFounder(String loginId) throws Exception {
        if (StringUtils.isBlank(loginId)) {
            loginId = loginId();
        }
        if (StringUtils.isBlank(loginId)) {
            return false;
        }
        LoginUser loginUser = user(loginId);
        if (loginUser == null) {
            return false;
        }
        return Objects.equals(loginUser.getFounder(), Constants.BOOL_TRUE);
    }

    public static boolean isFounder() throws Exception {
        return isFounder(loginId());
    }

    /**
     * 获取登录用户id
     *
     * @return
     * @throws Exception
     */
    public static String loginId() throws Exception {
        return loginId(token());
    }

    /**
     * 获取登录用户id
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static String loginId(String token) throws Exception {
        return (String) StpUtil.getLoginIdByToken(token);
    }

    /**
     * 获取前端传过来的token
     *
     * @return
     * @throws Exception
     */
    public static String token() throws Exception {
        return WebContext.getRequest().getHeader(MX_SECURITY_CONFIG.saTokenName());
    }

    public static LoginUser user(String loginId) throws Exception {
        String userKey = StrUtil.format(USER_INFO, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        return CACHE_STORAGE_ADAPTER.getUser(userKey);
    }

    public static LoginUser user() throws Exception {
        return user(loginId());
    }

    public static void lock(String loginId) throws Exception {
        String lockKey = StrUtil.format(LOCK_KEY, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        CACHE_STORAGE_ADAPTER.lock(lockKey, loginId);
    }

    public static void unlock(String loginId) throws Exception {
        String lockKey = StrUtil.format(LOCK_KEY, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        CACHE_STORAGE_ADAPTER.unlock(lockKey);
    }

    public static boolean checkLock(String loginId) throws Exception {
        String lockKey = StrUtil.format(LOCK_KEY, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        return CACHE_STORAGE_ADAPTER.checkLock(lockKey);
    }

    public static List<String> permissionList(Object loginId) throws Exception {
        String permissionKey = StrUtil.format(PERMISSION_LIST, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        return CACHE_STORAGE_ADAPTER.permissionList(permissionKey);
    }

    public static void cachePermission(Object loginId, List<String> permissionList) throws Exception {
        if (loginId == null || permissionList == null) {
            return;
        }
        String permissionKey = StrUtil.format(PERMISSION_LIST, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        CACHE_STORAGE_ADAPTER.cachePermission(permissionKey, permissionList);
    }

    public static void cacheUser(LoginUser loginUser) throws Exception {
        if (loginUser == null) {
            throw new RuntimeException("loginUser is null");
        }
        String userKey = StrUtil.format(USER_INFO, MX_SECURITY_CONFIG.project(), loginUser.getClient(), MX_SECURITY_CONFIG.device(), loginUser.getId());
        CACHE_STORAGE_ADAPTER.cacheUser(userKey, loginUser);
    }

    public static void clearPermission() throws Exception {
        String loginId = loginId();
        if (StringUtils.isBlank(loginId)) {
            return;
        }
        String permissionKey = StrUtil.format(PERMISSION_LIST, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        CACHE_STORAGE_ADAPTER.clearPermission(permissionKey);
    }

    public static void clearUser() throws Exception {
        String loginId = loginId();
        if (StringUtils.isBlank(loginId)) {
            return;
        }
        String userKey = StrUtil.format(USER_INFO, MX_SECURITY_CONFIG.project(), MX_SECURITY_CONFIG.client(), MX_SECURITY_CONFIG.device(), loginId);
        CACHE_STORAGE_ADAPTER.clearUser(userKey);
    }


}
