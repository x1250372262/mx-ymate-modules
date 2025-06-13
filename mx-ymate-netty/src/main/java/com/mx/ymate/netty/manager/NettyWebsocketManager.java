package com.mx.ymate.netty.manager;

import com.mx.ymate.netty.bean.WebsocketConfig;
import com.mx.ymate.netty.impl.NettyWebsocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/9.
 * @Time: 15:31.
 * @Description:
 */
public class NettyWebsocketManager {

    private final Log LOG = LogFactory.getLog(NettyWebsocketManager.class);
    private final WebsocketConfig websocketConfig;
    private NettyWebsocket nettyWebsocket;

    public NettyWebsocketManager(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }


    public void init() {
        if (nettyWebsocket != null) {
            LOG.warn("NettyWebsocket已初始化，忽略重复初始化");
            return;
        }
        nettyWebsocket = new NettyWebsocket(websocketConfig).init();
    }

    public void start() {
        if (nettyWebsocket == null) {
            LOG.warn("NettyWebsocket没有初始化，请先初始化");
            return;
        }
        try {
            nettyWebsocket.start();
        } catch (Exception e) {
            LOG.error("NettyWebsocket启动失败", e);
        }
    }


    public void stop() {
        if (nettyWebsocket != null) {
            nettyWebsocket.stop();
        }
    }
}
