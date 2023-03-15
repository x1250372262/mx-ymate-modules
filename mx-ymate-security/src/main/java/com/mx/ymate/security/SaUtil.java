package com.mx.ymate.security;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.satoken.ISaTokenConfig;
import com.mx.ymate.satoken.SaToken;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.base.model.SecurityUser;
import net.ymate.platform.commons.json.JsonWrapper;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 11:59
 * @Description:
 */
public class SaUtil {

    /**
     * redis缓存中key token名称:user-info:客户端:用户id
     */
    public final static String USER_INFO = "{}:mxsecurity:user-info:{}:{}";
    /**
     * redis缓存权限列表 token名称:permission:客户端:token:类型:用户id
     */
    public final static String PERMISSION_LIST = "{}:mxsecurity:permission:{}:{}:{}:{}";

    private final static ISecurityConfig MX_SECURITY_CONFIG = Security.get().getConfig();
    private final static ISaTokenConfig SA_TOKEN_CONFIG = SaToken.get().getConfig();

    public static boolean isFounder(String loginId, String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            token = token();
        }
        return Objects.equals(user(loginId, token).getFounder(), Constants.BOOL_TRUE);
    }

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

    public static String loginId() throws Exception {
        return loginId(token());
    }

    public static String loginId(String token) throws Exception {
        return (String) StpUtil.getLoginIdByToken(token);
    }

    public static String token() throws Exception {
        return WebContext.getRequest().getHeader(SA_TOKEN_CONFIG.tokenName());
    }

    public static LoginUser user(String loginId, String token) throws Exception {
        String userKey = StrUtil.format(USER_INFO,SA_TOKEN_CONFIG.tokenName(), MX_SECURITY_CONFIG.client(), loginId);
        return JsonWrapper.deserialize(RedisApi.strGet(userKey),LoginUser.class);
    }

    public static LoginUser user(String loginId) throws Exception {
        return user(loginId, token());
    }

    public static LoginUser user() throws Exception {
        return user(loginId());
    }

    public static String getTokenName() {
        return SA_TOKEN_CONFIG.tokenName();
    }


    public static String getToken() {
        return SA_TOKEN_CONFIG.tokenName();
    }

}
