package com.mx.ymate.netty.handler.connection;

import com.mx.ymate.netty.impl.NettyClient;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/11.
 * @Time: 13:42.
 * @Description:
 */
public class ClientConnectionManager extends AbstractConnectionManager {

    protected final Map<String, NettyClient> NETTY_CLIENT_MAP = new ConcurrentHashMap<>();

    public static final ClientConnectionManager INSTANCE = new ClientConnectionManager();

    public static ClientConnectionManager getInstance() {
        return INSTANCE;
    }

    public void addClient(ChannelHandlerContext ctx, NettyClient nettyClient) {
        NETTY_CLIENT_MAP.put(ctx.channel().id().asShortText(), nettyClient);
    }

    public NettyClient getClient(ChannelHandlerContext ctx) {
        return NETTY_CLIENT_MAP.get(ctx.channel().id().asShortText());
    }

    public void removeClient(ChannelHandlerContext ctx) {
        NETTY_CLIENT_MAP.remove(ctx.channel().id().asShortText());
    }

    public void clearClient() {
        NETTY_CLIENT_MAP.clear();
    }

}
