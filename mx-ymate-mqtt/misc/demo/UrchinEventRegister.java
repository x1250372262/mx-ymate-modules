package com.sft.urchin.station.event;

import com.mx.ymate.dev.support.event.IYmpInitializer;
import com.mx.ymate.dev.support.event.annotation.Initializer;
import com.mx.ymate.mqtt.Mqtt;
import net.ymate.platform.core.ApplicationEvent;

/**
 * @author lidong
 * @version 1.0
 * @date 2025-02-12 11:51
 */
@Initializer(value = 1)
public class UrchinEventRegister implements IYmpInitializer {

    @Override
    public void startup(ApplicationEvent applicationEvent) {

    }

    @Override
    public void initialized(ApplicationEvent applicationEvent) {
        //获取本地数据库有所的mqtt配置
        //list 循环 构建 configbean
        //for
//        QueueUtil.start();

        Mqtt.get().getManager().connectAll();;
    }

    @Override
    public void destroyed(ApplicationEvent applicationEvent) {
//        QueueUtil.stop();
    }
}
