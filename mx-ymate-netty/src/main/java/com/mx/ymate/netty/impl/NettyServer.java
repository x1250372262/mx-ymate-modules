package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.handler.HeartBeatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.log.Logs;

import java.util.List;
import java.util.Objects;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class NettyServer {

    private final INettyConfig config;

    private final EventLoopGroup BOSS_GROUP = new NioEventLoopGroup();
    private final EventLoopGroup WORK_GROUP = new NioEventLoopGroup();

    public NettyServer(INettyConfig config) {
        this.config = config;
    }

    public void run() throws Exception {
        if (config.serverDecoder() == null) {
            throw new Exception("请指定decoder解码器");
        }
        if (CollUtil.isEmpty(config.serverHandler())) {
            throw new Exception("请指定handler处理类");
        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS_GROUP, WORK_GROUP)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline channelPipeline = ch.pipeline();
                if (Objects.nonNull(config.serverHeartBeatTime())) {
                    channelPipeline.addLast(new IdleStateHandler(config.serverHeartBeatTime(), 0, 0));
                }
                channelPipeline.addLast(config.serverDecoder());
                for (ChannelInboundHandlerAdapter clazz : config.serverHandler()) {
                    channelPipeline.addLast(clazz);
                }
                if (Objects.nonNull(config.serverHeartBeatTime())) {
                    channelPipeline.addLast(new HeartBeatServerHandler());
                }
            }
        });
        if (Objects.nonNull(config.serverPort())) {
//            //绑定端口，同步等待成功
            serverBootstrap.bind(config.serverPort()).sync();
            Logs.get().getLogger().info(StrUtil.format("单端口{}绑定成功", config.serverPort()));
        } else if (Objects.nonNull(config.serverStartPort()) && Objects.nonNull(config.serverEndPort())) {
            int startPort = config.serverStartPort();
            int endPort = config.serverEndPort();
            List<String> excludePorts = config.serverExcludePort();
            for (; startPort <= endPort; startPort++) {
                //绑定端口，同步等待成功
                if (!excludePorts.contains(BlurObject.bind(startPort).toStringValue())) {
                    serverBootstrap.bind(startPort).sync();
                    Logs.get().getLogger().info(StrUtil.format("多端口{}绑定成功", startPort));
                }
            }
        }

    }

    public void stop() {
        //优雅退出，释放线程池
        BOSS_GROUP.shutdownGracefully();
        WORK_GROUP.shutdownGracefully();
    }

}
