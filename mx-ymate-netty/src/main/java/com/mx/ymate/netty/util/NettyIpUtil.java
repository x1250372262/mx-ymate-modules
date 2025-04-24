package com.mx.ymate.netty.util;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class NettyIpUtil {


    public static InetSocketAddress getInetSocketAddress(ChannelHandlerContext ctx) {
        return (InetSocketAddress) ctx.channel().remoteAddress();
    }

    public static InetSocketAddress getInetSocketAddress(String key) {
        ChannelHandlerContext ctx = NettyUtil.getContent(key);
        if (ctx == null) {
            return null;
        }
        return (InetSocketAddress) ctx.channel().remoteAddress();
    }

    public static String getHost(ChannelHandlerContext ctx) {
        return getInetSocketAddress(ctx).getHostString();
    }

    public static int getPort(ChannelHandlerContext ctx) {
        return getInetSocketAddress(ctx).getPort();
    }

}
