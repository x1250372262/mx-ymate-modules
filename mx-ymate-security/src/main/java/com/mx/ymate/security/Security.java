package com.mx.ymate.security;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.listener.SaTokenEventCenter;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.code.SecurityCode;
import com.mx.ymate.security.impl.DefaultSecurityConfig;
import com.mx.ymate.security.satoken.*;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;

public final class Security implements IModule, ISecurity {

    public static final String DEFAULT_PASSWORD = "123456";


    private static volatile ISecurity instance;

    private IApplication owner;

    private ISecurityConfig config;

    private boolean initialized;

    public static ISecurity get() {
        ISecurity inst = instance;
        if (inst == null) {
            synchronized (Security.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Security.class);
                }
            }
        }
        return inst;
    }

    public Security() {
    }

    public Security(ISecurityConfig config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize(IApplication owner) throws Exception {
        if (!initialized) {
            //
            YMP.showVersion("Initializing mx-ymate-satoken-satoken-${version}", new Version(1, 0, 0, Security.class, Version.VersionType.Alpha));
            //
            this.owner = owner;
            if (config == null) {
                IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
                if (configureFactory != null) {
                    IApplicationConfigurer configurer = configureFactory.getConfigurer();
                    IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                    if (moduleConfigurer != null) {
                        config = DefaultSecurityConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultSecurityConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
                if (config == null) {
                    throw new RuntimeException("获取安全模块配置失败");
                }
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }
            if (config.isEnabled()) {
                //集成初始化
                // 注入上下文Bean
                SaManager.setSaTokenContext(new YmpContext());
                SaManager.setConfig(config.toSaTokenConfig());
                SaManager.setSaTokenDao(new RedisDao());
                SaManager.setSaJsonTemplate(new FastJsonTemplate());
                // 注入权限
                SaManager.setStpInterface(new RedisStp());
                //注入MxSaTokenListener
                SaTokenEventCenter.registerListener(new MxSaTokenListener());
            }
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void close() throws Exception {
        if (initialized) {
            initialized = false;
            //
            config = null;
            owner = null;
        }
    }

    @Override
    public IApplication getOwner() {
        return owner;
    }

    @Override
    public ISecurityConfig getConfig() {
        return config;
    }

    /**
     * 检查是否有问题
     *
     * @param mxResult
     * @return
     */
    public static boolean error(MxResult mxResult) {
        return mxResult == null || SecurityCode.SECURITY_CHECK_ERROR.code().equals(mxResult.code());
    }
}
