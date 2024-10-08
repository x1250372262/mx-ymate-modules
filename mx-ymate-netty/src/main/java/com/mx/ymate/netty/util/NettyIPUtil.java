package com.mx.ymate.netty.util;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * @Author: mengxiang.
 * @create: 2022-04-29 13:25
 * @Description:
 */
public class NettyIPUtil {


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
