package com.mx.ymate.netty.option;

import io.netty.bootstrap.ServerBootstrap;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/10.
 * @Time: 11:27.
 * @Description:
 */
public interface IServerOptionConfig {

    /**
     * 配置option
     * @param serverBootstrap
     */
    void optionConfig(ServerBootstrap serverBootstrap);

}
