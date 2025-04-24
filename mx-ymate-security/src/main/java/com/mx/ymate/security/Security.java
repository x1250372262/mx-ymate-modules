package com.mx.ymate.security;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.base.code.SecurityCode;
import com.mx.ymate.security.impl.DefaultSecurityConfig;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
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
            YMP.showVersion("Initializing mx-ymate-security-${version}", new Version(1, 0, 0, Security.class, Version.VersionType.Alpha));
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

    public static MxResult error() {
        return MxResult.create(SecurityCode.SECURITY_CHECK_ERROR);
    }
}
