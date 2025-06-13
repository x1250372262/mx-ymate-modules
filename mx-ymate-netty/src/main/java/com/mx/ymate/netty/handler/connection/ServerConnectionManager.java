package com.mx.ymate.netty.handler.connection;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/11.
 * @Time: 13:42.
 * @Description:
 */
public class ServerConnectionManager extends AbstractConnectionManager {

    private static final ServerConnectionManager INSTANCE = new ServerConnectionManager();

    public static ServerConnectionManager getInstance() {
        return INSTANCE;
    }
}
