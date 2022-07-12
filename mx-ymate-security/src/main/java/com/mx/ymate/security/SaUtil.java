package com.mx.ymate.security;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.satoken.ISaTokenConfig;
import com.mx.ymate.satoken.SaToken;
import com.mx.ymate.security.base.model.SecurityUser;
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
     * redis缓存中key user-info:客户端:用户id
     */
    public final static String USER_INFO = "user-info:{}:{}";
    /**
     * redis缓存权限列表 permission:客户端:token:类型:用户id
     */
    public final static String PERMISSION_LIST = "permission:{}:{}:{}:{}";

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
        return Objects.equals(user(loginId).getFounder(), Constants.BOOL_TRUE);
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

    public static SecurityUser user(String loginId, String token) throws Exception {
        String userKey = StrUtil.format(USER_INFO, MX_SECURITY_CONFIG.client(), loginId);
        return (SecurityUser) StpUtil.getTokenSessionByToken(token).get(userKey);
    }

    public static SecurityUser user(String loginId) throws Exception {
        return user(loginId, token());
    }

    public static SecurityUser user() throws Exception {
        return user(loginId());
    }

    public static String getTokenName() {
        return SA_TOKEN_CONFIG.tokenName();
    }


    public static String getToken() {
        return SA_TOKEN_CONFIG.tokenName();
    }

}
