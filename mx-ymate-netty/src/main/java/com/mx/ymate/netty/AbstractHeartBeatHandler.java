package com.mx.ymate.netty;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.netty.impl.NettyServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.ymate.platform.commons.lang.BlurObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * @Author: xujianpeng.
 * @Date 2024/11/18.
 * @Time: 10:11.
 * @Description:
 */
@ChannelHandler.Sharable
public abstract class AbstractHeartBeatHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(AbstractHeartBeatHandler.class);

    private final static String MESSAGE = "netty异常，ip:{},端口:{}";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 检查 ctx 和 evt 是否为空
        if (ctx == null || evt == null) {
            LOG.warn("Context or event is null, skipping event handling.");
            return;
        }
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                onReaderIdle(ctx);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                onWriterIdle(ctx);
            } else if (event.state() == IdleState.ALL_IDLE) {
                onAllIdle(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private void onHandle(ChannelHandlerContext ctx){
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        LOG.debug(StrUtil.format("ip:{},端口:{}客户端长时间未发送消息", ipSocket.getHostString(), ipSocket.getPort()));
        ctx.channel().close();
    }

    /**
     * 读空闲触发的逻辑（默认空实现）
     */
    public void onReaderIdle(ChannelHandlerContext ctx) {
        onHandle(ctx);
    }

    /**
     * 写空闲触发的逻辑（默认空实现）
     */
    public void onWriterIdle(ChannelHandlerContext ctx) {
        onHandle(ctx);
    }

    /**
     * 读写空闲触发的逻辑（默认空实现）
     */
    public void onAllIdle(ChannelHandlerContext ctx) {
        onHandle(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String ip = "未知";
        String port = "未知";
        if (ctx != null) {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            ip = ipSocket.getHostString();
            port = BlurObject.bind(ipSocket.getPort()).toString();
        }
        LOG.error(StrUtil.format(MESSAGE, ip, port), cause);
        throw new RuntimeException(StrUtil.format(MESSAGE, ip, port), cause);
    }


    public static class DefaultServerHeartImpl extends AbstractHeartBeatHandler{

    }

    public static class DefaultClientHeartImpl extends AbstractHeartBeatHandler{

    }

    public static class DefaultWebsocketHeartImpl extends AbstractHeartBeatHandler{

    }
}
