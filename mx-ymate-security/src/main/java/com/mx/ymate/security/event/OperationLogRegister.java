package com.mx.ymate.security.event;

import com.mx.ymate.security.base.model.SecurityOperationLog;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;

import static com.mx.ymate.security.base.config.SecurityConstants.LOG_EVENT_KEY;

/**
 * @Author: mengxiang.
 * @create: 2021-09-06 17:20
 * @Description:
 */
@EventRegister
public class OperationLogRegister implements IEventRegister {


    @Override
    public void register(Events events) throws Exception {
        events.registerEvent(OperationLogEvent.class);
        // 订阅模块事件：异步
        events.registerListener(Events.MODE.ASYNC, OperationLogEvent.class, (IEventListener<OperationLogEvent>) context -> {
            if (context.getEventName() == OperationLogEvent.EVENT.CREATE_LOG) {
                SecurityOperationLog securityOperationLog = (SecurityOperationLog) context.getParamExtend(LOG_EVENT_KEY);
                if (securityOperationLog != null) {
                    try {
                        securityOperationLog.save();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return false;
        });
    }
}
