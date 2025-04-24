package com.mx.ymate.dev;

import com.mx.ymate.dev.impl.DefaultMxDevConfig;
import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
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
public final class MxDev implements IModule, IMxDev {

    private static volatile IMxDev instance;

    private IApplication owner;

    private IMxDevConfig config;

    private boolean initialized;

    public static IMxDev get() {
        IMxDev inst = instance;
        if (inst == null) {
            synchronized (MxDev.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(MxDev.class);
                }
            }
        }
        return inst;
    }

    public MxDev() {
    }

    public MxDev(IMxDevConfig config) {
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
            YMP.showVersion("Initializing mx-ymate-mxDev-${version}", new Version(1, 0, 0, MxDev.class, Version.VersionType.Alpha));
            //
            this.owner = owner;
            if (config == null) {
                IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
                if (configureFactory != null) {
                    IApplicationConfigurer configurer = configureFactory.getConfigurer();
                    IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                    if (moduleConfigurer != null) {
                        config = DefaultMxDevConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultMxDevConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
                if (config == null) {
                    throw new RuntimeException("获取核心配置模块配置失败");
                }
            }
            if (!config.isInitialized()) {
                config.initialize(this);
            }
            initialized = true;
            if(config.isInitialized()){
                I18nHelper.init();
            }
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
    public IMxDevConfig getConfig() {
        return config;
    }

}
