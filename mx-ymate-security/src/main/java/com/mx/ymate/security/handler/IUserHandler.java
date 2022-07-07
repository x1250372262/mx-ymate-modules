package com.mx.ymate.security.handler;

import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.security.base.enums.ResourceType;
import net.ymate.platform.core.beans.annotation.Bean;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2021-10-27 15:03
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
     * @return
     * @throws Exception
     */
    MxResult createAfter(Map<String, String> params) throws Exception;

    /**
     * 构建resourceId
     *
     * @param resourceType
     * @return
     * @throws Exception
     */
    String buildResourceId(ResourceType resourceType) throws Exception;

    @Bean
    class DefaultUserHandler implements IUserHandler {


        @Override
        public MxResult createBefore(Map<String, String> params) throws Exception {
            return MxResult.ok();
        }

        @Override
        public MxResult createAfter(Map<String, String> params) throws Exception {
            return MxResult.ok();
        }

        @Override
        public String buildResourceId(ResourceType resourceType) throws Exception {
            return null;
        }
    }

}
