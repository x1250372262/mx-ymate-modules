package com.mx.ymate.dev.support.event;

import net.ymate.platform.core.ApplicationEvent;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface IYmpInitializer {

    /**
     * 应用容器启动事件
     *
     * @param applicationEvent
     * @throws Exception
     */
    void startup(ApplicationEvent applicationEvent);

    /**
     * 应用容器初始化事件
     *
     * @param applicationEvent
     * @throws Exception
     */
    void initialized(ApplicationEvent applicationEvent);

    /**
     * 应用容器销毁事件
     *
     * @param applicationEvent
     * @throws Exception
     */
    void destroyed(ApplicationEvent applicationEvent);
}
