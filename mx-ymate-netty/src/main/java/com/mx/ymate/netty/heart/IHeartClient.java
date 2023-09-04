package com.mx.ymate.netty.heart;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-07 09:43
 * @Description:
 */
public interface IHeartClient {

    void handle(ChannelHandlerContext ctx, Object evt) throws Exception;

}
