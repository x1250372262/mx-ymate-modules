package com.mx.ymate.netty.bean;

import com.mx.ymate.dev.util.ConfigUtil;
import com.mx.ymate.netty.heart.AbstractHeartBeatHandler;
import com.mx.ymate.netty.heart.DefaultWebsocketHeartImpl;
import com.mx.ymate.netty.option.IWebsocketOptionConfig;
import com.mx.ymate.netty.option.impl.WebsocketOptionConfig;

import java.util.List;
import java.util.stream.Collectors;

import static com.mx.ymate.dev.constants.Constants.XG;
import static com.mx.ymate.netty.INettyConfig.HEART_BEAT_TIME_ITEM_COUNT;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/5.
 * @Time: 13:05.
 * @Description:
 */
public class WebsocketConfig {

    public static final String AUTO_INIT = "websocket.autoInit";
    private static final String PORT = "websocket.port";
    private static final String HEART_BEAT_TIME = "websocket.heartBeatTime";
    private static final String HEART_BEAT_CLASS = "websocket.heartBeatClass";
    private static final String MAPPING = "websocket.mapping";
    private static final String HANDLER_PACKAGE = "websocket.handlerPackage";
    private static final String OPTION_CONFIG_CLASS = "websocket.optionConfigClass";


    /**
     * websocket端口 默认8756
     */
    private Integer port;

    /**
     * 心跳维护时间（三个时间）格式：读空闲|写空闲|读写空闲 默认空不维护 单位s
     */
    private List<Integer> heartBeatTimeList;

    /**
     * 心跳实现类 当heartBeatTime不等于0的时候有效 继承com.mx.ymate.netty.heart.AbstractHeartBeatHandler类
     */
    private AbstractHeartBeatHandler heartBeatHandler;

    /**
     * websocket请求路径 默认websocket
     */
    private String mapping;

    /**
     * websocket 处理器所在包 多个用|分割
     */
    private List<String> packageList;

    /**
     * option配置器
     */
    private IWebsocketOptionConfig websocketOptionConfig;

    public static WebsocketConfig buildConfig(ConfigUtil configUtil) {
        WebsocketConfig websocketConfig = new WebsocketConfig();
        websocketConfig.setPort(configUtil.getInt(PORT, 8756));
        List<String> heartBeatTimeTempList = configUtil.getList(HEART_BEAT_TIME);
        if (heartBeatTimeTempList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
            websocketConfig.setHeartBeatTimeList(heartBeatTimeTempList.stream().map(Integer::valueOf).collect(Collectors.toList()));
        }
        AbstractHeartBeatHandler websocketHeart = configUtil.getClassImpl(HEART_BEAT_CLASS, AbstractHeartBeatHandler.class);
        if (websocketHeart == null) {
            websocketHeart = new DefaultWebsocketHeartImpl();
        }
        websocketConfig.setHeartBeatHandler(websocketHeart);
        String websocketMapping = configUtil.getString(MAPPING, "/websocket");
        if (!websocketMapping.startsWith(XG)) {
            websocketMapping = XG + websocketMapping;
        }
        websocketConfig.setMapping(websocketMapping);
        websocketConfig.setPackageList(configUtil.getList(HANDLER_PACKAGE));
        IWebsocketOptionConfig optionConfig = configUtil.getClassImpl(OPTION_CONFIG_CLASS, IWebsocketOptionConfig.class);
        if (optionConfig == null) {
            optionConfig = new WebsocketOptionConfig();
        }
        websocketConfig.setWebsocketOptionConfig(optionConfig);
        return websocketConfig;
    }

    private WebsocketConfig() {
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<Integer> getHeartBeatTimeList() {
        return heartBeatTimeList;
    }

    public void setHeartBeatTimeList(List<Integer> heartBeatTimeList) {
        this.heartBeatTimeList = heartBeatTimeList;
    }

    public AbstractHeartBeatHandler getHeartBeatHandler() {
        return heartBeatHandler;
    }

    public void setHeartBeatHandler(AbstractHeartBeatHandler heartBeatHandler) {
        this.heartBeatHandler = heartBeatHandler;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public List<String> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<String> packageList) {
        this.packageList = packageList;
    }

    public IWebsocketOptionConfig getWebsocketOptionConfig() {
        return websocketOptionConfig;
    }

    public void setWebsocketOptionConfig(IWebsocketOptionConfig websocketOptionConfig) {
        this.websocketOptionConfig = websocketOptionConfig;
    }
}
