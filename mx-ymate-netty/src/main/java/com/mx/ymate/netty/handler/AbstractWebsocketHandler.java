package com.mx.ymate.netty.handler;

import com.mx.ymate.netty.IMxWebsocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.ymate.platform.log.ILogger;
import net.ymate.platform.log.Logs;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: mengxiang.
 * @create: 2024-05-14 14:00
 * @Description:
 */
public abstract class AbstractWebsocketHandler implements IMxWebsocketHandler {

    private static final Log LOG = LogFactory.getLog(AbstractWebsocketHandler.class);

    @Override
    public void onOpen(ChannelHandlerContext ctx) throws Exception {
        LOG.info("onOpen");
    }

    /**
     * 消息到达
     * @param ctx 会话
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public abstract void onMessage(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception;

    @Override
    public void onClose(ChannelHandlerContext ctx) throws Exception {
        LOG.info("onClose");
    }

    @Override
    public void onError(ChannelHandlerContext ctx, Throwable t) throws Exception {
        LOG.info("onError");
    }
}
