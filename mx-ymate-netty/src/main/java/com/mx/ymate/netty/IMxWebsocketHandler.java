package com.mx.ymate.netty;

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
     *
     * @param ctx 会话
     */
    void onOpen(ChannelHandlerContext ctx) throws Exception;

    /**
     * 连接收到消息
     *
     * @param ctx 会话
     * @param msg 消息
     */
    void onMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;

    /**
     * 连接关闭
     *
     * @param ctx 会话
     */
    void onClose(ChannelHandlerContext ctx) throws Exception;

    /**
     * 连接发生错误
     *
     * @param ctx 会话
     * @param t   异常
     */
    void onError(ChannelHandlerContext ctx, Throwable t) throws Exception;

    /**
     * 发送消息
     *
     * @param ctx     会话
     * @param message 消息
     */
    default void sendMessage(ChannelHandlerContext ctx, String message) throws Exception {
        ctx.writeAndFlush(new TextWebSocketFrame(message));
    }
}
