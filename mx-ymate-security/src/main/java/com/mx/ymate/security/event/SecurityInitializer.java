package com.mx.ymate.security.event;

import cn.dev33.satoken.SaManager;
import com.mx.ymate.dev.support.event.IYmpInitializer;
import com.mx.ymate.dev.support.event.annotation.Initializer;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.satoken.FastJsonTemplate;
import com.mx.ymate.security.satoken.YmpContext;
import net.ymate.platform.core.ApplicationEvent;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Initializer(value = -100)
public class SecurityInitializer implements IYmpInitializer {

    @Override
    public void startup(ApplicationEvent applicationEvent) {

    }

    @Override
    public void initialized(ApplicationEvent applicationEvent) {
        ISecurityConfig config = Security.get().getConfig();
        if (config != null && config.isEnabled() && config.isInitialized()) {
            //集成初始化
            // 注入上下文Bean
            SaManager.setSaTokenContext(new YmpContext());
            SaManager.setConfig(config.toSaTokenConfig());
            SaManager.setSaJsonTemplate(new FastJsonTemplate());
            //注入dao和权限
            config.cacheStoreAdapter().init();
        }
    }

    @Override
    public void destroyed(ApplicationEvent applicationEvent) {

    }
}
