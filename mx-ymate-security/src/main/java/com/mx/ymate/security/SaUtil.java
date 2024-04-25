package com.mx.ymate.security;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.base.config.SecurityConstants;
import net.ymate.platform.commons.json.JsonWrapper;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mx.ymate.security.base.config.SecurityConstants.USER_INFO;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 11:59
 * @Description:
 */
public class SaUtil {

    private final static ISecurityConfig MX_SECURITY_CONFIG = Security.get().getConfig();

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
        return WebContext.getRequest().getHeader(getTokenName());
    }

    public static LoginUser user(String loginId) throws Exception {
        String userKey = StrUtil.format(USER_INFO, getTokenName(), MX_SECURITY_CONFIG.client(), loginId);
        return JsonWrapper.deserialize(RedisApi.strGet(userKey), LoginUser.class);
    }

    public static LoginUser user() throws Exception {
        return user(loginId());
    }

    public static void lock(String loginId) throws Exception{
        RedisApi.strSet(loginId,loginId);
    }

    public static void unlock(String loginId) throws Exception{
        RedisApi.strDelete(loginId);
    }

    public static boolean checkLock(String loginId) throws Exception{
        String data = RedisApi.strGet(loginId);
        return StringUtils.isNotBlank(data);
    }

    public static List<String> permissionList(Object loginId) throws Exception {
        List<String> permissionList = new ArrayList<>();
        String permissionKey = StrUtil.format(SecurityConstants.PERMISSION_LIST, getTokenName(), MX_SECURITY_CONFIG.client(), loginId);
        String permissionStr = RedisApi.strGet(permissionKey);
        if (StringUtils.isNotBlank(permissionStr)) {
            JSONArray redisPermissionList = JSONObject.parseArray(permissionStr);
            for (Object redisRole : redisPermissionList) {
                permissionList.add(Convert.toStr(redisRole));
            }
        }
        return permissionList;
    }

    public static void cachePermission(Object loginId, List<String> permissionList) throws Exception {
        if (loginId == null || permissionList == null) {
            return;
        }
        String permissionKey = StrUtil.format(SecurityConstants.PERMISSION_LIST, getTokenName(), MX_SECURITY_CONFIG.client(), loginId);
        RedisApi.strDelete(permissionKey);
        RedisApi.strSet(permissionKey, JSONObject.toJSONString(permissionList));
    }

    public static void cacheUser(LoginUser loginUser) throws Exception {
        if (loginUser == null) {
            throw new RuntimeException("loginUser is null");
        }
        String userKey = StrUtil.format(USER_INFO, getTokenName(), loginUser.getClient(), loginUser.getId());
        RedisApi.strDelete(userKey);
        RedisApi.strSet(userKey, JsonWrapper.toJsonString(loginUser));
    }


    /**
     * 获取token名称
     *
     * @return
     */
    public static String getTokenName() {
        return MX_SECURITY_CONFIG.saTokenName();
    }

}
