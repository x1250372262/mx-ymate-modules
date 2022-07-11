package com.mx.ymate.serv;

import net.ymate.platform.serv.ISessionWrapper;
import net.ymate.platform.serv.nio.server.NioSessionWrapper;

import java.io.IOException;

/**
 * @Author: 徐建鹏.
 * @create: 2022-07-07 18:18
 * @Description:
 */
public interface IServerHandler {


    void onSessionIdleRemoved(NioSessionWrapper sessionWrapper);


    void onSessionRegistered(NioSessionWrapper session) throws IOException;


    void onSessionAccepted(NioSessionWrapper session) throws IOException;


    void onBeforeSessionClosed(NioSessionWrapper session) throws IOException;


    void onAfterSessionClosed(NioSessionWrapper session) throws IOException;


    void onMessageReceived(Object message, NioSessionWrapper session) throws IOException;


    void onExceptionCaught(Throwable e, NioSessionWrapper session) throws IOException;
}
