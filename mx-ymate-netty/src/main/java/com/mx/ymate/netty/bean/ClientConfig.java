package com.mx.ymate.netty.bean;

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.netty.handler.IHandlerRegistrar;
import com.mx.ymate.netty.handler.NettyHandlerUtil;
import com.mx.ymate.netty.heart.AbstractHeartBeatHandler;
import com.mx.ymate.netty.heart.DefaultClientHeartImpl;
import com.mx.ymate.netty.option.IClientOptionConfig;
import com.mx.ymate.netty.option.impl.ClientOptionConfig;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

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
public class ClientConfig {

    public static final String NAME = "client.name";
    public static final String AUTO_INIT = "client.autoInit";
    private static final String REMOTE_ADDRESS = "remoteAddress";
    private static final String RECONNECT_ENABLED = "reconnect.enabled";
    private static final String RECONNECT_MAX_ATTEMPTS = "reconnect.maxAttempts";
    private static final String RECONNECT_INTERVAL = "reconnect.interval";
    private static final String HEART_BEAT_TIME = "heartBeatTime";
    private static final String HEART_BEAT_CLASS = "heartBeatClass";
    private static final String HANDLER_PACKAGE = "handlerPackage";
    private static final String HANDLER_REGISTRAR_CLASS = "handlerRegistrarClass";
    private static final String OPTION_CONFIG_CLASS = "optionConfigClass";
    public static final AttributeKey<ClientContext> CLIENT_CTX_KEY = AttributeKey.valueOf("CLIENT_CONTEXT");
    public static final String REMOTE_ADDRESS_KEY = "REMOTE_ADDRESS_KEY";

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 远程连接地址 ip:port  多个用逗号分割
     */
    private List<String> remoteAddress;

    /**
     * 是否开启断线重连 默认false
     */
    private boolean reconnectEnabled;

    /**
     * 最大重连次数  默认3 小于等于0代表无限次
     */
    private int reconnectMaxAttempts;

    /**
     * 重连间隔 默认10s
     */
    private int reconnectInterval;

    /**
     * 处理器列表 排序之后的
     */
    private List<HandlerConfig> handlerConfigList;

    /**
     * option配置器
     */
    private IClientOptionConfig clientOptionConfig;

    public static ClientConfig buildConfig(String name, ConfigUtil configUtil) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setName(name);
        clientConfig.setRemoteAddress(configUtil.getList(REMOTE_ADDRESS));
        clientConfig.setReconnectEnabled(configUtil.getBool(RECONNECT_ENABLED, false));
        clientConfig.setReconnectMaxAttempts(configUtil.getInt(RECONNECT_MAX_ATTEMPTS, 3));
        clientConfig.setReconnectInterval(configUtil.getInt(RECONNECT_INTERVAL, 10));
        List<HandlerConfig> handlerConfigs = NettyHandlerUtil.findAllHandlerConfig(configUtil.getList(HANDLER_PACKAGE), configUtil.getClassImpl(HANDLER_REGISTRAR_CLASS, IHandlerRegistrar.class));
        List<String> heartBeatTimeTempList = configUtil.getList(HEART_BEAT_TIME);
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            //闲时检测
            List<Integer> heartTimeList = heartBeatTimeTempList.stream().map(Integer::valueOf).collect(Collectors.toList());
            handlerConfigs.add(new HandlerConfig(() -> new IdleStateHandler(heartTimeList.get(0), heartTimeList.get(1), heartTimeList.get(2)), 100, false));
            //心跳
            AbstractHeartBeatHandler heartBeatHandler = configUtil.getClassImpl(HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
            if (heartBeatHandler == null) {
                heartBeatHandler = new DefaultClientHeartImpl();
            }
            handlerConfigs.add(new HandlerConfig(heartBeatHandler, 101, true));
        }

        handlerConfigs.sort(Comparator
//                // 先按类型优先级
//                .comparingInt((HandlerConfig c) -> c.getType().getSortValue())
                //再按照排序字段
                .comparingInt(HandlerConfig::getOrder));

        clientConfig.setHandlerConfigList(handlerConfigs);
        IClientOptionConfig optionConfig = configUtil.getClassImpl(OPTION_CONFIG_CLASS, IClientOptionConfig.class);
        if (optionConfig == null) {
            optionConfig = new ClientOptionConfig();
        }
        clientConfig.setClientOptionConfig(optionConfig);
        return clientConfig;
    }

    private ClientConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(List<String> remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public boolean isReconnectEnabled() {
        return reconnectEnabled;
    }

    public void setReconnectEnabled(boolean reconnectEnabled) {
        this.reconnectEnabled = reconnectEnabled;
    }

    public int getReconnectMaxAttempts() {
        return reconnectMaxAttempts;
    }

    public void setReconnectMaxAttempts(int reconnectMaxAttempts) {
        this.reconnectMaxAttempts = reconnectMaxAttempts;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public List<HandlerConfig> getHandlerConfigList() {
        return handlerConfigList;
    }

    public void setHandlerConfigList(List<HandlerConfig> handlerConfigList) {
        this.handlerConfigList = handlerConfigList;
    }

    public IClientOptionConfig getClientOptionConfig() {
        return clientOptionConfig;
    }

    public void setClientOptionConfig(IClientOptionConfig clientOptionConfig) {
        this.clientOptionConfig = clientOptionConfig;
    }
}
