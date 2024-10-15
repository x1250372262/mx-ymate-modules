package com.mx.ymate.security.base.config;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class SecurityConstants {

    /**
     * 用户信息缓存key token名称:userInfo:客户端:用户id
     */
    public final static String USER_INFO = "{}:userInfo:{}:{}";

    /**
     * 缓存权限列表缓存key token名称:permission:客户端:用户id
     */
    public final static String PERMISSION_LIST = "{}:permission:{}:{}";

    /**
     * 锁定key token名称:lockKey:客户端:用户id
     */
    public final static String LOCK_KEY = "{}:lockKey:{}:{}";

    /**
     * log key
     */
    public final static String LOG_EVENT_KEY = "mxLog";

}
