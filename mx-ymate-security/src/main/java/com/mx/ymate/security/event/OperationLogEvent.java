package com.mx.ymate.security.event;

import net.ymate.platform.core.event.AbstractEventContext;
import net.ymate.platform.core.event.IEvent;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
