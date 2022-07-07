package com.mx.ymate.security;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.constants.Constants;
import com.mx.ymate.satoken.ISaTokenConfig;
import com.mx.ymate.satoken.SaToken;
import com.mx.ymate.security.base.model.SecurityUser;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Author: mengxiang.
 * @create: 2021-09-04 11:59
 * @Description:
 */
@Bean
public class SaUtil {

    /**
     * redis缓存中key user-info:客户端:用户id
     */
    public final static String USER_INFO = "user-info:{}:{}";
    /**
     * redis缓存权限列表 permission:客户端:token:类型:用户id
     */
    public final static String PERMISSION_LIST = "permission:{}:{}:{}:{}";

    private final ISecurityConfig mxSecurityConfig = Security.get().getConfig();
    private final ISaTokenConfig saTokenConfig = SaToken.get().getConfig();

    public boolean isFounder(String loginId, String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            token = token();
        }
        return Objects.equals(user(loginId, token).getFounder(), Constants.BOOL_TRUE);
    }

    public boolean isFounder(String loginId) throws Exception {
        if (StringUtils.isBlank(loginId)) {
            loginId = loginId();
        }
        return Objects.equals(user(loginId).getFounder(), Constants.BOOL_TRUE);
    }

    public boolean isFounder() throws Exception {
        return isFounder(loginId());
    }

    public String loginId() throws Exception {
        return loginId(token());
    }

    public String loginId(String token) throws Exception {
        return (String) StpUtil.getLoginIdByToken(token);
    }

    public String token() throws Exception {
        return WebContext.getRequest().getHeader(saTokenConfig.tokenName());
    }

    public SecurityUser user(String loginId, String token) throws Exception {
        String userKey = StrUtil.format(USER_INFO, mxSecurityConfig.client(), loginId);
        return (SecurityUser) StpUtil.getTokenSessionByToken(token).get(userKey);
    }

    public SecurityUser user(String loginId) throws Exception {
        return user(loginId, token());
    }

    public SecurityUser user() throws Exception {
        return user(loginId());
    }

    public String getTokenName() {
        return saTokenConfig.tokenName();
    }


    public String getToken() {
        return saTokenConfig.tokenName();
    }

}
