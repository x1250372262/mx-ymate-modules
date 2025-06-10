package com.mx.ymate.netty.bean;

import cn.hutool.core.collection.CollUtil;
import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.netty.handler.IHandlerRegistrar;
import com.mx.ymate.netty.handler.NettyHandlerUtil;
import com.mx.ymate.netty.heart.AbstractHeartBeatHandler;
import com.mx.ymate.netty.heart.DefaultServerHeartImpl;
import com.mx.ymate.netty.option.IServerOptionConfig;
import com.mx.ymate.netty.option.impl.ServerOptionConfig;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mx.ymate.netty.INettyConfig.HEART_BEAT_TIME_ITEM_COUNT;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/5.
 * @Time: 13:05.
 * @Description:
 */
public class ServerConfig {

    public static final String NAME = "server.name";
    public static final String AUTO_INIT = "server.autoInit";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String HEART_BEAT_TIME = "heartBeatTime";
    private static final String HEART_BEAT_CLASS = "heartBeatClass";
    private static final String HANDLER_PACKAGE = "handlerPackage";
    private static final String HANDLER_REGISTRAR_CLASS = "handlerRegistrarClass";
    private static final String OPTION_CONFIG_CLASS = "optionConfigClass";

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务地址 默认127.0.0.1
     */
    private String host;

    /**
     * 服务端端口
     */
    private Integer port;

    /**
     * 处理器列表 排序之后的
     */
    private List<HandlerConfig> handlerConfigList;

    /**
     * option配置器
     */
    private IServerOptionConfig serverOptionConfig;

    public static ServerConfig buildConfig(String name, ConfigUtil configUtil) {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setName(name);
        serverConfig.setHost(configUtil.getString(HOST,"127.0.0.1"));
        serverConfig.setPort(configUtil.getInteger(PORT));
        List<HandlerConfig> handlerConfigs = NettyHandlerUtil.findAllHandlerConfig(configUtil.getList(HANDLER_PACKAGE),configUtil.getClassImpl(HANDLER_REGISTRAR_CLASS, IHandlerRegistrar.class));
        List<String> heartBeatTimeTempList = configUtil.getList(HEART_BEAT_TIME);
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            //闲时检测
            List<Integer> heartTimeList = heartBeatTimeTempList.stream().map(Integer::valueOf).collect(Collectors.toList());
            handlerConfigs.add(new HandlerConfig(new IdleStateHandler(heartTimeList.get(0), heartTimeList.get(1), heartTimeList.get(2)) ,100,true));
            //心跳
            AbstractHeartBeatHandler heartBeatHandler = configUtil.getClassImpl(HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
            if (heartBeatHandler == null) {
                heartBeatHandler = new DefaultServerHeartImpl();
            }
            handlerConfigs.add(new HandlerConfig(heartBeatHandler,101,true));
        }

        handlerConfigs.sort(Comparator
//                // 先按类型优先级
//                .comparingInt((HandlerConfig c) -> c.getType().getSortValue())
                //再按照排序字段
                .comparingInt(HandlerConfig::getOrder));
        serverConfig.setHandlerConfigList(handlerConfigs);
        IServerOptionConfig optionConfig = configUtil.getClassImpl(OPTION_CONFIG_CLASS, IServerOptionConfig.class);
        if (optionConfig == null) {
            optionConfig = new ServerOptionConfig();
        }
        serverConfig.setServerOptionConfig(optionConfig);
        return serverConfig;
    }

    private ServerConfig() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<HandlerConfig> getHandlerConfigList() {
        return handlerConfigList;
    }

    public void setHandlerConfigList(List<HandlerConfig> handlerConfigList) {
        this.handlerConfigList = handlerConfigList;
    }

    public IServerOptionConfig getServerOptionConfig() {
        return serverOptionConfig;
    }

    public void setServerOptionConfig(IServerOptionConfig serverOptionConfig) {
        this.serverOptionConfig = serverOptionConfig;
    }
}
