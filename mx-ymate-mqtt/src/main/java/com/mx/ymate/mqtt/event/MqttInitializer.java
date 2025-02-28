package com.mx.ymate.mqtt.event;

import com.mx.ymate.dev.support.event.IYmpInitializer;
import com.mx.ymate.dev.support.event.annotation.Initializer;
import com.mx.ymate.mqtt.IMqtt;
import com.mx.ymate.mqtt.IMqttConfig;
import com.mx.ymate.mqtt.Mqtt;
import net.ymate.platform.core.ApplicationEvent;

/**
 * @Author: mengxiang.
 * @create: 2021-09-06 17:20
 * @Description:
 */
@Initializer(value = -99)
public class MqttInitializer implements IYmpInitializer {

    @Override
    public void startup(ApplicationEvent applicationEvent) {

    }

    @Override
    public void initialized(ApplicationEvent applicationEvent) {
        IMqtt iMqtt = Mqtt.get();
        IMqttConfig config = iMqtt.getConfig();
        if (config.isEnabled() && config.autoConnect()) {
            //等待框架启动成功
            iMqtt.connect();
        }
    }

    @Override
    public void destroyed(ApplicationEvent applicationEvent) {

    }
}
