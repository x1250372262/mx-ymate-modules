package com.mx.ymate.mqtt.event;

import com.mx.ymate.mqtt.IMqtt;
import com.mx.ymate.mqtt.IMqttConfig;
import com.mx.ymate.mqtt.Mqtt;
import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;

/**
 * @Author: mengxiang.
 * @create: 2021-09-06 17:20
 * @Description:
 */
@EventRegister
public class MqttInitializer implements IEventRegister {
    @Override
    public void register(Events events) throws Exception {
        // 订阅模块事件：异步
        events.registerListener(Events.MODE.ASYNC, ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
            if (context.getEventName() == ApplicationEvent.EVENT.APPLICATION_INITIALIZED) {
                IMqtt iMqtt = Mqtt.get();
                IMqttConfig config = iMqtt.getConfig();
                if (config.isEnabled() && config.autoConnect()) {
                    //等待框架启动成功
                    iMqtt.connect();
                }

            }
            return false;
        });
    }
}
