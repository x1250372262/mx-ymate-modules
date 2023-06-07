package com.mx.ymate.netty.heart;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-07 09:43
 * @Description:
 */
public interface IHeartServer {

    void handle(ChannelHandlerContext ctx, Object evt) throws Exception;

}
