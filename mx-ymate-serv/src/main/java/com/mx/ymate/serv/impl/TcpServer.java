package com.mx.ymate.serv.impl;

import com.mx.ymate.serv.IMxServConfig;
import com.mx.ymate.serv.IServerHandler;
import com.mx.ymate.serv.MxServ;
import net.ymate.platform.serv.IServerCfg;
import net.ymate.platform.serv.impl.DefaultSessionIdleChecker;
import net.ymate.platform.serv.nio.INioCodec;
import net.ymate.platform.serv.nio.server.INioSessionListener;
import net.ymate.platform.serv.nio.server.NioSessionManager;
import net.ymate.platform.serv.nio.server.NioSessionWrapper;

import java.io.IOException;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class TcpServer {

    private final IMxServConfig config;

    private NioSessionManager<NioSessionWrapper, Object> sessionManager;

    public TcpServer(IMxServConfig config) {
        this.config = config;
    }

    public NioSessionManager<NioSessionWrapper, Object> run() throws Exception {
        INioCodec codec = config.getCodec();
        if (codec == null) {
            throw new Exception("请配置编解码器");
        }

        IServerHandler serverHandler = config.getServerHandler();
        if (serverHandler == null) {
            throw new Exception("请配置服务端处理类");
        }

        IServerCfg serverCfg = config.toServerCfg();
        // 通过会话管理器创建服务端
        // 设置空闲会话检查服务
        sessionManager = new NioSessionManager<>(serverCfg, config.getCodec(), new INioSessionListener<NioSessionWrapper, Object>() {
            @Override
            public void onSessionIdleRemoved(NioSessionWrapper sessionWrapper) {
                serverHandler.onSessionIdleRemoved(sessionWrapper);
            }

            @Override
            public void onSessionRegistered(NioSessionWrapper session) throws IOException {
                serverHandler.onSessionRegistered(session);
            }

            @Override
            public void onSessionAccepted(NioSessionWrapper session) throws IOException {
                serverHandler.onSessionAccepted(session);
            }

            @Override
            public void onBeforeSessionClosed(NioSessionWrapper session) throws IOException {
                serverHandler.onBeforeSessionClosed(session);
            }

            @Override
            public void onAfterSessionClosed(NioSessionWrapper session) throws IOException {
                serverHandler.onAfterSessionClosed(session);
            }

            @Override
            public void onMessageReceived(Object message, NioSessionWrapper session) throws IOException {
                serverHandler.onMessageReceived(message, session);
            }

            @Override
            public void onExceptionCaught(Throwable e, NioSessionWrapper session) throws IOException {
                serverHandler.onExceptionCaught(e, session);
            }


        }, config.getServerKeepAliveTime());
        sessionManager.idleChecker(new DefaultSessionIdleChecker<>());
        sessionManager.initialize();
        return sessionManager;

    }

    public void stop() throws Exception {
        //优雅退出，释放线程池
        sessionManager.close();
    }

}
