package com.mx.ymate.security.handler;

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
public interface IUserHandler {

    /**
     * 添加用户之前
     *
     * @param params
     * @return
     * @throws Exception
     */
    MxResult createBefore(Map<String, String> params) throws Exception;

    /**
     * 添加用户之后
     *
     * @param params
     * @param securityUser
     * @return
     * @throws Exception
     */
    MxResult createAfter(Map<String, String> params, SecurityUser securityUser) throws Exception;


    @Bean
    class DefaultUserHandler implements IUserHandler {


        @Override
        public MxResult createBefore(Map<String, String> params) throws Exception {
            return MxResult.ok();
        }

        @Override
        public MxResult createAfter(Map<String, String> params, SecurityUser securityUser) throws Exception {
            return MxResult.ok();
        }
    }

}
