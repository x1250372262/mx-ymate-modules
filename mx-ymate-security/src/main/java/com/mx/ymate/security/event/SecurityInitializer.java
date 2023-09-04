package com.mx.ymate.security.event;
import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.listener.SaTokenEventCenter;
import com.mx.ymate.satoken.SaToken;
import com.mx.ymate.satoken.ymate.SaTokenContextForYmate;
import com.mx.ymate.satoken.ymate.SaTokenDaoRedis;
import com.mx.ymate.satoken.ymate.json.SaJsonTemplateForFastJson;
import com.mx.ymate.security.base.config.SecurityStpImpl;
import com.mx.ymate.security.dao.ISecurityOperationLogDao;
import com.mx.ymate.security.satoken.listener.MxSaTokenListener;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.IApplicationInitializer;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import net.ymate.platform.webmvc.util.ExceptionProcessHelper;
import net.ymate.platform.webmvc.util.IExceptionProcessor;

import static com.mx.ymate.dev.code.Code.NOT_LOGIN;
import static net.ymate.platform.core.ApplicationEvent.EVENT.APPLICATION_INITIALIZED;

/**
 * @Author: mengxiang.
 * @create: 2022-07-03 11:50
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
                //集成初始化
                // 注入上下文Bean
                SaManager.setSaTokenContext(new SaTokenContextForYmate());
                SaManager.setConfig(SaToken.get().getConfig().toSaTokenConfig());
                SaManager.setSaTokenDao(new SaTokenDaoRedis());
                SaManager.setSaJsonTemplate(new SaJsonTemplateForFastJson());
                // 注入权限
                SaManager.setStpInterface(YMP.get().getBeanFactory().getBean(SecurityStpImpl.class));
                //注入MxSaTokenListener
                SaTokenEventCenter.registerListener(YMP.get().getBeanFactory().getBean(MxSaTokenListener.class));
            }
            return false;
        });
    }
}
