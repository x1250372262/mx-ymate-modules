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
        nettyWebsocket = new NettyWebsocket(websocketConfig).init();
    }

    public void start() {
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
