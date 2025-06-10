package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.bean.HandlerConfig;

import java.util.List;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/4.
 * @Time: 16:10.
 * @Description:
 */
public interface IHandlerRegistrar {

    /**
     * 返回需要手动注册的 Handler 配置列表
     *
     * @return
     */
    List<HandlerConfig> registerHandlers();

}
