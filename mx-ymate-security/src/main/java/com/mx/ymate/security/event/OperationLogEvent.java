package com.mx.ymate.security.event;

import net.ymate.platform.core.event.AbstractEventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * @Author: mengxiang.
 * @create: 2021-09-06 17:20
 * @Description:
 */
public class OperationLogEvent extends AbstractEventContext<Object, OperationLogEvent.EVENT> implements IEvent {


    public enum EVENT {

        /**
         * 日志创建
         */
        CREATE_LOG,

    }

    public OperationLogEvent(Object owner, EVENT eventName) {
        super(owner, OperationLogEvent.class, eventName);
    }
}
