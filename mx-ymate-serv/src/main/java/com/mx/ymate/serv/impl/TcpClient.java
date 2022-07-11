package com.mx.ymate.serv.impl;

import com.mx.ymate.serv.IClientHandler;
import com.mx.ymate.serv.IMxServConfig;
import com.mx.ymate.serv.MxServ;
import net.ymate.platform.serv.IClient;
import net.ymate.platform.serv.IClientCfg;
import net.ymate.platform.serv.Servs;
import net.ymate.platform.serv.impl.DefaultHeartbeatServiceImpl;
import net.ymate.platform.serv.impl.DefaultReconnectServiceImpl;
import net.ymate.platform.serv.nio.INioCodec;
import net.ymate.platform.serv.nio.INioSession;
import net.ymate.platform.serv.nio.client.NioClient;
import net.ymate.platform.serv.nio.client.NioClientListener;

import java.io.IOException;

/**
 * @Author: mengxiang.
 * @create: 2022-04-22 15:27
 * @Description: netty服务类
 */
public class TcpClient {

    private final IMxServConfig config;

    private NioClient nioClient;

    public TcpClient(IMxServConfig config) {
        this.config = config;
    }

    public void run() throws Exception {
        INioCodec codec = config.getCodec();
        if (codec == null) {
            throw new Exception("请配置编解码器");
        }

        IClientHandler clientHandler = config.getClientHandler();
        if (clientHandler == null) {
            throw new Exception("请配置客户端处理类");
        }
        IClientCfg clientCfg = config.toClientCfg();
        nioClient = Servs.createClient(clientCfg, codec, new DefaultReconnectServiceImpl(), new DefaultHeartbeatServiceImpl(), new NioClientListener() {
            @Override
            public void onClientReconnected(IClient<?, ?> client) {
                clientHandler.onClientReconnected(client);
            }

            @Override
            public void onSessionRegistered(INioSession session) throws IOException {
                clientHandler.onSessionRegistered(session);
            }

            @Override
            public void onSessionConnected(INioSession session) throws IOException {
                super.onSessionConnected(session);
                clientHandler.onSessionConnected(session);
            }

            @Override
            public void onBeforeSessionClosed(INioSession session) throws IOException {
                clientHandler.onBeforeSessionClosed(session);
            }

            @Override
            public void onAfterSessionClosed(INioSession session) throws IOException {
                clientHandler.onAfterSessionClosed(session);
            }

            @Override
            public void onMessageReceived(Object message, INioSession session) throws IOException {
                super.onMessageReceived(message, session);
                clientHandler.onMessageReceived(message, session);
            }
        });
        nioClient.connect();
    }

    public void stop() throws Exception {
        //优雅退出，释放线程池
        nioClient.close();
    }


}
