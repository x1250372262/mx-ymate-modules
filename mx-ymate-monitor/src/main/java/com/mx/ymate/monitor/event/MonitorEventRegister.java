package com.mx.ymate.monitor.event;

import com.mx.ymate.monitor.IMonitorConfig;
import com.mx.ymate.monitor.Monitor;
import com.mx.ymate.monitor.enums.ClientEnum;
import com.mx.ymate.monitor.mq.RedisMq;
import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;

import static net.ymate.platform.core.ApplicationEvent.EVENT.APPLICATION_INITIALIZED;

@EventRegister
public class MonitorEventRegister implements IEventRegister {

    @Override
    public void register(Events events) {
        events.registerEvent(ApplicationEvent.class);
        // 订阅模块事件：默认同步
        events.registerListener(Events.MODE.ASYNC, ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
            IMonitorConfig config = Monitor.get().getConfig();
            if (context.getEventName() == APPLICATION_INITIALIZED) {
                //初始化设监听
                try {
                    if (ClientEnum.isClient(config.client())) {
                        RedisMq.subscribe(config.dataSubscribeListener());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        });
    }

}
