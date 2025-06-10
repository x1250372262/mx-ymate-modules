package com.mx.ymate.netty.option.impl;

import com.mx.ymate.netty.option.IServerOptionConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/10.
 * @Time: 11:31.
 * @Description:
 */
public class ServerOptionConfig implements IServerOptionConfig {
    @Override
    public void optionConfig(ServerBootstrap serverBootstrap) {
        serverBootstrap
                // TCP连接的最大等待队列数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 允许重复绑定地址
                .option(ChannelOption.SO_REUSEADDR, true)
                // 接收缓冲区大小
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 关闭 Nagle 算法，降低延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 保持连接，避免 NAT 清理连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 接收缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 发送缓冲区大小
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
                // 防止 TIME_WAIT 状态端口不能重用
                .childOption(ChannelOption.SO_REUSEADDR, true);
    }
}
