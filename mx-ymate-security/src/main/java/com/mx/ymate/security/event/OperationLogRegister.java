package com.mx.ymate.security.event;

import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.dao.ISecurityOperationLogDao;
import net.ymate.platform.core.YMP;
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
public class OperationLogRegister implements IEventRegister {

    private final ISecurityOperationLogDao iSecurityOperationLogDao = YMP.get().getBeanFactory().getBean(ISecurityOperationLogDao.class);


    @Override
    public void register(Events events) throws Exception {
        events.registerEvent(OperationLogEvent.class);
        // 订阅模块事件：异步
        events.registerListener(Events.MODE.ASYNC, OperationLogEvent.class, (IEventListener<OperationLogEvent>) context -> {
            if (context.getEventName() == OperationLogEvent.EVENT.CREATE_LOG) {
                SecurityOperationLog securityOperationLog = (SecurityOperationLog) context.getParamExtend("log");
                if (securityOperationLog != null) {
                    try {
                        iSecurityOperationLogDao.create(securityOperationLog);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return false;
        });
    }
}
