package com.mx.ymate.netty.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.INettyConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import net.ymate.platform.log.Logs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.mx.ymate.netty.INettyConfig.HEART_BEAT_TIME_ITEM_COUNT;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class NettyClient {

    private final INettyConfig config;

    private EventLoopGroup workGroup;
    private Bootstrap bootstrap;
    private List<RemoteAddress> remoteAddressesList;

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
        if (config.clientDecoder() == null) {
            throw new Exception("请指定decoder解码器");
        }
        if (CollUtil.isEmpty(config.clientHandler())) {
            throw new Exception("请指定handler处理类");
        }
        List<String> remoteAddressStrList = config.clientRemoteAddress();
        if (CollUtil.isEmpty(remoteAddressStrList)) {
            throw new Exception("请指定需要连接的服务地址");
        }
        remoteAddressesList = getRemoteAddressList(remoteAddressStrList);
        if (CollUtil.isEmpty(remoteAddressesList)) {
            throw new Exception("请指定需要连接的服务地址");
        }
        if (bootstrap == null) {
            bootstrap = new Bootstrap();
        }
        if (workGroup == null) {
            workGroup = new NioEventLoopGroup();
        }
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        channelPipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        List<Integer> heartBeatTimeList = config.clientHeartBeatTimeList();
                        if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
                            channelPipeline.addLast(new IdleStateHandler(heartBeatTimeList.get(0), heartBeatTimeList.get(1), heartBeatTimeList.get(2)));
                        }
                        channelPipeline.addLast(config.clientDecoder());
                        for (ChannelInboundHandlerAdapter clazz : config.clientHandler()) {
                            channelPipeline.addLast(clazz);
                        }
                        if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
                            channelPipeline.addLast(config.clientHeart());
                        }
                    }
                });

        connect();

    }

    public void connect(RemoteAddress remoteAddress) throws Exception {
        Logs.get().getLogger().info(StrUtil.format("和ip:{},端口:{}服务进行连接", remoteAddress.getHost(), remoteAddress.getPort()));
        ChannelFuture cf = bootstrap.connect(remoteAddress.getHost(), remoteAddress.getPort());
        cf.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                //重连交给后端线程执行
                future.channel().eventLoop().schedule(() -> {
                    Logs.get().getLogger().error("重连服务端...");
                    try {
                        connect(remoteAddress);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, 3, TimeUnit.SECONDS);
            } else {
                Logs.get().getLogger().info("服务端连接成功...");
            }
        });
    }

    private void connect() {
        int clientNum = config.clientNum();
        if(clientNum <= 0){
            return;
        }
       for(int clientIndex = 0; clientIndex < clientNum; clientIndex++){
           //启动客户端去连接服务器端
           for (RemoteAddress remoteAddress : remoteAddressesList) {
               ThreadUtil.execAsync(() -> {
                   try {
                       connect(remoteAddress);
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
               });
           }
       }
    }


    public void stop() {
        //优雅退出，释放线程池
        workGroup.shutdownGracefully();
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
