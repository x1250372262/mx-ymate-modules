package com.mx.ymate.security.event;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.listener.SaTokenEventCenter;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.satoken.*;
import net.ymate.platform.core.ApplicationEvent;
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
public class SecurityInitializer implements IEventRegister {
    @Override
    public void register(Events events) throws Exception {
        events.registerEvent(ApplicationEvent.class);
        // 订阅模块事件：异步
        events.registerListener(Events.MODE.ASYNC, ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
            if (context.getEventName() == ApplicationEvent.EVENT.APPLICATION_INITIALIZED) {
                ISecurityConfig config = Security.get().getConfig();
                if (config != null && config.isEnabled() && config.isInitialized()) {
                    //集成初始化
                    // 注入上下文Bean
                    SaManager.setSaTokenContext(new YmpContext());
                    SaManager.setConfig(config.toSaTokenConfig());
                    SaManager.setSaTokenDao(new RedisDao());
                    SaManager.setSaJsonTemplate(new FastJsonTemplate());
                    // 注入权限
                    SaManager.setStpInterface(new RedisStp());
                    //注入MxSaTokenListener
                    SaTokenEventCenter.registerListener(YMP.get().getBeanFactory().getBean(MxSaTokenListener.class));
                }

            }
            return false;
        });
    }
}
