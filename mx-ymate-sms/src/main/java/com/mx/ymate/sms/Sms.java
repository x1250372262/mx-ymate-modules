package com.mx.ymate.sms;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.sms.adapter.ISmsAdapter;
import com.mx.ymate.sms.impl.DefaultSmsConfig;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;

import java.util.List;

import static com.mx.ymate.sms.code.SmsCode.CHANEL_ERROR;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 09:36.
 * @Description:
 */
public final class Sms implements IModule, ISms {


    private static volatile ISms instance;

    private IApplication owner;

    private ISmsConfig config;

    private boolean initialized;


    public static ISms get() {
        ISms inst = instance;
        if (inst == null) {
            synchronized (Sms.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(Sms.class);
                }
            }
        }
        return inst;
    }

    public Sms() {
    }

    public Sms(ISmsConfig config) {
        this.config = config;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public void initialize(IApplication owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            if (config == null) {
                IApplicationConfigureFactory configureFactory = owner.getConfigureFactory();
                if (configureFactory != null) {
                    IApplicationConfigurer configurer = configureFactory.getConfigurer();
                    IModuleConfigurer moduleConfigurer = configurer == null ? null : configurer.getModuleConfigurer(MODULE_NAME);
                    if (moduleConfigurer != null) {
                        config = DefaultSmsConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultSmsConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
            }
            initialized = true;
            YMP.showVersion("初始化 mx-ymate-sms-${version} 模块成功", new Version(1, 0, 0, Sms.class, Version.VersionType.Release));
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
            config = null;
            owner = null;
        }
    }

    @Override
    public IApplication getOwner() {
        return owner;
    }

    @Override
    public ISmsConfig getConfig() {
        return config;
    }

    @Override
    public MxResult send(String channel, String mobile, Object params) throws Exception {
        ISmsAdapter smsAdapter = config.smsAdapter(channel);
        if (smsAdapter == null) {
            return MxResult.create(CHANEL_ERROR);
        }
        return smsAdapter.send(mobile, params);
    }

    @Override
    public MxResult send(String channel, List<String> mobileList, Object params) throws Exception {
        ISmsAdapter smsAdapter = config.smsAdapter(channel);
        if (smsAdapter == null) {
            return MxResult.create(CHANEL_ERROR);
        }
        return smsAdapter.send(mobileList, params);
    }

    @Override
    public MxResult send(String mobile, Object params) throws Exception {
        return send("default", mobile, params);
    }

    @Override
    public MxResult send(List<String> mobileList, Object params) throws Exception {
        return send("default", mobileList, params);
    }

}
