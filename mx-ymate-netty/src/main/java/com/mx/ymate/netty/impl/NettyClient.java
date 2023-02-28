package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.mx.ymate.netty.INettyConfig;
import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.handler.HeartBeatServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class NettyClient {

    private final INettyConfig config;

    private final EventLoopGroup WORK_GROUP = new NioEventLoopGroup();

    public NettyClient(INettyConfig config) {
        this.config = config;
    }

    private List<RemoteAddress> getRemoteAddressList(List<String> remoteAddressStrList) {
        List<RemoteAddress> list = new ArrayList<>();
        for (String remoteAddressStr : remoteAddressStrList) {
            String[] remoteAddressArr = remoteAddressStr.split(":");
            if (remoteAddressArr.length == 2) {
                list.add(new RemoteAddress(remoteAddressArr[0], Convert.toInt(remoteAddressArr[1])));
            }
        }
        return list;
    }


    public void run() throws Exception {
        List<String> remoteAddressStrList = config.clientRemoteAddress();
        if (CollUtil.isEmpty(remoteAddressStrList)) {
            throw new Exception("请指定需要连接的服务地址");
        }
        List<RemoteAddress> remoteAddressList = getRemoteAddressList(remoteAddressStrList);
        if (CollUtil.isEmpty(remoteAddressList)) {
            throw new Exception("请指定需要连接的服务地址");
        }
        if (config.clientDecoder() == null) {
            throw new Exception("请指定handler处理类");
        }
        if (CollUtil.isEmpty(config.clientHandler())) {
            throw new Exception("请指定handler处理类");
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(WORK_GROUP)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        if (Objects.nonNull(config.clientHeartBeatTime())) {
                            channelPipeline.addLast(new IdleStateHandler(config.clientHeartBeatTime(), 0, 0, TimeUnit.SECONDS));
                        }
                        channelPipeline.addLast(config.clientDecoder());
                        for (ChannelInboundHandlerAdapter clazz : config.clientHandler()) {
                            channelPipeline.addLast(clazz);
                        }
                        if (Objects.nonNull(config.clientHeartBeatTime())) {
                            channelPipeline.addLast(new HeartBeatServerHandler());
                        }
                    }
                });
        for (RemoteAddress remoteAddress : remoteAddressList) {
            bootstrap.connect(remoteAddress.getHost(), remoteAddress.getPort()).sync();
        }

    }

    public void stop() {
        //优雅退出，释放线程池
        WORK_GROUP.shutdownGracefully();
    }


    public static class RemoteAddress {

        public RemoteAddress(String host, int port) {
            this.host = host;
            this.port = port;
        }

        private String host;

        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

}
