package com.mx.ymate.security.base.config;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityConstants {

    /**
     * 用户信息缓存key 项目名称:客户端:设备:userInfo:用户id
     */
    public final static String USER_INFO = "{}:{}:{}:userInfo:{}";

    /**
     * 缓存权限列表缓存key 项目名称:客户端:设备:permission:用户id
     */
    public final static String PERMISSION_LIST = "{}:{}:{}:permission:{}";

    /**
     * 锁定key 项目名称:客户端:设备:lockKey:用户id
     */
    public final static String LOCK_KEY = "{}:{}:{}:lockKey:{}";

    /**
     * 二维码key  项目名称:qrcodeKey:登录key
     */
    public final static String QRCODE_KEY = "{}:qrcodeKey:{}";

    /**
     * log key
     */
    public final static String LOG_EVENT_KEY = "mxLog";


}
