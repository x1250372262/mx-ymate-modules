package com.mx.ymate.serv;

import net.ymate.platform.serv.IClient;
import net.ymate.platform.serv.nio.INioSession;

import java.io.IOException;

/**
 * @Author: mengxiang.
 * @create: 2022-07-07 18:18
 * @Description:
 */
public interface IClientHandler {

    void onClientReconnected(IClient<?, ?> client);

    void onSessionRegistered(INioSession session) throws IOException;

    void onSessionConnected(INioSession session) throws IOException;

    void onBeforeSessionClosed(INioSession session) throws IOException;

    void onAfterSessionClosed(INioSession session) throws IOException;

    void onMessageReceived(Object message, INioSession session) throws IOException;
}
