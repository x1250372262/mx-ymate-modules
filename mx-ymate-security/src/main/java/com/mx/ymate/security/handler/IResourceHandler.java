package com.mx.ymate.security.handler;

import com.mx.ymate.security.base.enums.ResourceType;
import net.ymate.platform.core.beans.annotation.Bean;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface IResourceHandler {


    /**
     * 构建resourceId
     *
     * @param resourceType
     * @param loginId
     * @return
     * @throws Exception
     */
    String buildResourceId(ResourceType resourceType, String loginId) throws Exception;

    /**
     * 构建resourceId
     *
     * @param resourceType
     * @param params
     * @return
     * @throws Exception
     */
    String buildResourceIdNoLogin(ResourceType resourceType, Map<String, String> params) throws Exception;

    @Bean
    class DefaultUserHandler implements IResourceHandler {


        @Override
        public String buildResourceId(ResourceType resourceType, String loginId) throws Exception {
            return null;
        }

        @Override
        public String buildResourceIdNoLogin(ResourceType resourceType, Map<String, String> params) throws Exception {
            return null;
        }
    }

}
