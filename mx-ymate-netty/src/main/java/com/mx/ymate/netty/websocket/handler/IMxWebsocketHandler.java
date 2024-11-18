package com.mx.ymate.netty.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description: websocket处理接口
 */
public interface IMxWebsocketHandler {


    /**
     * 连接打开
     * @param ctx
     * @throws Exception
     */
    void onOpen(ChannelHandlerContext ctx) throws Exception;

    /**
     * 连接收到消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    void onMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;

    /**
     * 连接关闭
     * @param ctx
     * @throws Exception
     */
    void onClose(ChannelHandlerContext ctx) throws Exception;

    /**
     * 连接发生错误
     * @param ctx
     * @param t
     * @throws Exception
     */
    void onError(ChannelHandlerContext ctx, Throwable t) throws Exception;

    /**
     * 发送消息
     * @param ctx
     * @param message
     * @throws Exception
     */
    default void sendMessage(ChannelHandlerContext ctx, String message) throws Exception {
        ctx.writeAndFlush(new TextWebSocketFrame(message));
    }
}
