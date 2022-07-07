package com.mx.ymate.security.event;

import cn.dev33.satoken.SaManager;
import com.mx.ymate.satoken.SaToken;
import com.mx.ymate.satoken.ymate.SaTokenContextForYmate;
import com.mx.ymate.satoken.ymate.SaTokenDaoRedis;
import com.mx.ymate.satoken.ymate.json.SaJsonTemplateForFastJson;
import com.mx.ymate.security.base.config.SecurityStpImpl;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.dao.ISecurityOperationLogDao;
import com.mx.ymate.security.satoken.listener.MxSaTokenListener;
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
public class InitRegister implements IEventRegister {

    private final ISecurityOperationLogDao iSecurityOperationLogDao = YMP.get().getBeanFactory().getBean(ISecurityOperationLogDao.class);


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
                SaManager.setSaTokenListener(YMP.get().getBeanFactory().getBean(MxSaTokenListener.class));
            }
            return false;
        });
    }
}
