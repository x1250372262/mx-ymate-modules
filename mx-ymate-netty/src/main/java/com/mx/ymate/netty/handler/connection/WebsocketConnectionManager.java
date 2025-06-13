package com.mx.ymate.netty.handler.connection;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/11.
 * @Time: 13:42.
 * @Description:
 */
public class WebsocketConnectionManager extends AbstractConnectionManager {

    private static final WebsocketConnectionManager INSTANCE = new WebsocketConnectionManager();

    public static WebsocketConnectionManager getInstance() {
        return INSTANCE;
    }
}
