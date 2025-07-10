package com.mx.ymate.netty.heart;

import com.mx.ymate.netty.Netty;
import com.mx.ymate.netty.bean.ClientConfig;
import com.mx.ymate.netty.bean.ClientContext;
import com.mx.ymate.netty.bean.RemoteAddress;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.mx.ymate.netty.bean.ClientConfig.CLIENT_CTX_KEY;

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
        LOG.warn("连接断开");
        ClientContext clientContext = ctx.channel().attr(CLIENT_CTX_KEY).get();
        if (clientContext == null) {
            return;
        }
        ClientConfig clientConfig = Netty.get().getConfig().clientConfig(clientContext.getClientId());
        if (clientConfig == null) {
            return;
        }
        if (clientConfig.isReconnectEnabled()) {
            LOG.warn("准备重连...");
            RemoteAddress remoteAddress = (RemoteAddress) clientContext.getExtra(ClientConfig.REMOTE_ADDRESS_KEY);
            if (remoteAddress == null) {
                LOG.warn("找不到重连地址，取消重连");
                return;
            }
            Netty.get().clientManager().reconnect(clientContext.getClientId(), remoteAddress, clientContext.getAllExtras());
        }
        super.channelInactive(ctx);
    }
}
