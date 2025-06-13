package com.mx.ymate.netty.heart;

import com.mx.ymate.netty.Netty;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/9.
 * @Time: 09:04.
 * @Description:
 */
@ChannelHandler.Sharable
public class DefaultClientHeartImpl extends AbstractHeartBeatHandler {

    private static final Log LOG = LogFactory.getLog(DefaultClientHeartImpl.class);


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.warn("连接断开，准备重连...");
        reconnect(ctx.channel().eventLoop());
        super.channelInactive(ctx);
    }

    private void reconnect(EventLoop eventLoop) {
        eventLoop.schedule(() -> {
            try {
                LOG.info("尝试重连服务端 " + host + ":" + port);
                Netty.get().clientManager().connect();
                bootstrap.connect(new InetSocketAddress(host, port)).addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        LOG.info("重连成功！");
                    } else {
                        LOG.error("重连失败，将在5秒后再次尝试...");
                        reconnect(eventLoop);
                    }
                });
            } catch (Exception e) {
                LOG.error("重连异常：", e);
                reconnect(eventLoop);
            }
        }, 5, TimeUnit.SECONDS);
    }
}
