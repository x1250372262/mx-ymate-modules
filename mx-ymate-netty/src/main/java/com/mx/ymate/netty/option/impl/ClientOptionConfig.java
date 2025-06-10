package com.mx.ymate.netty.option.impl;

import com.mx.ymate.netty.option.IClientOptionConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/10.
 * @Time: 11:32.
 * @Description:
 */
public class ClientOptionConfig implements IClientOptionConfig {
    @Override
    public void optionConfig(Bootstrap bootstrap) {
        bootstrap
                // 关闭 Nagle 算法
                .option(ChannelOption.TCP_NODELAY, true)
                // 保持连接
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 接收缓冲区
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 发送缓冲区
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .option(ChannelOption.SO_REUSEADDR, true);
    }
}
