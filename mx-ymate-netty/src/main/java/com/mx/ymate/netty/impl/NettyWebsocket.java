package com.mx.ymate.netty.impl;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.ymate.platform.log.Logs;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class NettyWebsocket {

    private final INettyConfig config;

    private final EventLoopGroup BOSS_GROUP = new NioEventLoopGroup();
    private final EventLoopGroup WORK_GROUP = new NioEventLoopGroup();

    public NettyWebsocket(INettyConfig config) {
        this.config = config;
    }

    public void run() throws Exception {
        if (config.websocketHandler() == null) {
            throw new Exception("请指定websocket handler处理类");
        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS_GROUP, WORK_GROUP)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast(new HttpServerCodec());
                        channelPipeline.addLast(new HttpObjectAggregator(8192));
                        channelPipeline.addLast(new WebSocketServerProtocolHandler("/" + config.websocketMapping()));
                        channelPipeline.addLast(config.websocketHandler());
                    }
                });
        serverBootstrap.bind(config.websocketPort()).sync();
        Logs.get().getLogger().info(StrUtil.format("端口{}websocket启动成功", config.websocketPort()));

    }

    public void stop() {
        //优雅退出，释放线程池
        BOSS_GROUP.shutdownGracefully();
        WORK_GROUP.shutdownGracefully();
    }

}
