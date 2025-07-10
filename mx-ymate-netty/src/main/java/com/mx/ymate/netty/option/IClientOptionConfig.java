package com.mx.ymate.netty.option;


import io.netty.bootstrap.Bootstrap;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/10.
 * @Time: 11:27.
 * @Description:
 */
public interface IClientOptionConfig {

    /**
     * 配置option
     *
     * @param bootstrap
     */
    void optionConfig(Bootstrap bootstrap);

}
