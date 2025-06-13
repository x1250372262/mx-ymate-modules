package com.mx.ymate.netty.util;

import com.mx.ymate.netty.handler.connection.AbstractConnectionManager;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 远程地址获取工具类
 */
public class NettyRemoteAddressUtil {

    public static InetSocketAddress getInetSocketAddress(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }
        SocketAddress remoteAddress = ctx.channel().remoteAddress();
        if (remoteAddress instanceof InetSocketAddress) {
            return (InetSocketAddress) remoteAddress;
        }
        return null;
    }

    public static InetSocketAddress getInetSocketAddress(String key) {
        ChannelHandlerContext ctx = AbstractConnectionManager.getContext(key); // 确认 ConnectionManager 类名
        if (ctx == null) {
            return null;
        }
        return getInetSocketAddress(ctx);
    }

    public static String getHost(ChannelHandlerContext ctx) {
        InetSocketAddress address = getInetSocketAddress(ctx);
        if (address == null) {
            return null;
        }
        return address.getHostString();
    }

    public static int getPort(ChannelHandlerContext ctx) {
        InetSocketAddress address = getInetSocketAddress(ctx);
        if (address == null) {
            return -1;
        }
        return address.getPort();
    }

}
