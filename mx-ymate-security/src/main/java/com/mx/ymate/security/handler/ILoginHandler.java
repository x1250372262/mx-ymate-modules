package com.mx.ymate.security.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.model.SecurityUser;
import net.ymate.platform.core.beans.annotation.Bean;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ILoginHandler {

    /**
     * 登录之前
     *
     * @param params
     * @return
     * @throws Exception
     */
    MxResult loginBefore(Map<String, String> params) throws Exception;

    /**
     * 登录成功
     *
     * @param params
     * @param securityUser
     * @return
     * @throws Exception
     */
    MxResult loginSuccess(Map<String, String> params, SecurityUser securityUser) throws Exception;

    /**
     * 登录失败
     *
     * @param params
     * @param securityUser
     * @return
     * @throws Exception
     */
    MxResult loginFail(Map<String, String> params, SecurityUser securityUser) throws Exception;


    /**
     * 登录完成事件
     *
     * @param params
     * @param securityUser
     * @param saTokenInfo
     * @return
     * @throws Exception
     */
    void loginComplete(Map<String, String> params, SecurityUser securityUser, SaTokenInfo saTokenInfo) throws Exception;

    /**
     * 退出之前
     *
     * @param params
     * @param loginId
     * @return
     * @throws Exception
     */
    MxResult logoutBefore(Map<String, String> params, String loginId) throws Exception;

    /**
     * 退出之后
     *
     * @param params
     * @param loginId
     * @return
     * @throws Exception
     */
    MxResult logoutAfter(Map<String, String> params, String loginId) throws Exception;

    /**
     * 自定义检查登录逻辑 默认不处理 验证失败抛出notLogin异常
     *
     * @param securityUser
     * @throws NotLoginException
     */
    void checkLoginCustom(SecurityUser securityUser) throws NotLoginException;


    @Bean
    class DefaultLoginHandler implements ILoginHandler {

        @Override
        public MxResult loginBefore(Map<String, String> params) throws Exception {
            return MxResult.ok();
        }

        @Override
        public MxResult loginSuccess(Map<String, String> params, SecurityUser securityUser) throws Exception {
            return MxResult.ok();
        }

        @Override
        public MxResult loginFail(Map<String, String> params, SecurityUser securityUser) throws Exception {
            return MxResult.ok();
        }

        @Override
        public void loginComplete(Map<String, String> params, SecurityUser securityUser, SaTokenInfo saTokenInfo) throws Exception {
        }

        @Override
        public MxResult logoutBefore(Map<String, String> params, String loginId) throws Exception {
            return MxResult.ok();
        }

        @Override
        public MxResult logoutAfter(Map<String, String> params, String loginId) throws Exception {
            return MxResult.ok();
        }

        @Override
        public void checkLoginCustom(SecurityUser securityUser) throws NotLoginException {

        }
    }
}
