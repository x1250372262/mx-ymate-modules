package com.mx.ymate.netty.option.impl;

import com.mx.ymate.netty.option.IWebsocketOptionConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/10.
 * @Time: 11:33.
 * @Description:
 */
public class WebsocketOptionConfig implements IWebsocketOptionConfig {
    @Override
    public void optionConfig(ServerBootstrap serverBootstrap) {
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)

                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
    }

}
