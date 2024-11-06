package com.mx.ymate.upload;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.impl.DefaultMxUploadConfig;
import net.ymate.platform.core.*;
import net.ymate.platform.core.module.IModule;
import net.ymate.platform.core.module.IModuleConfigurer;
import net.ymate.platform.core.module.impl.DefaultModuleConfigurer;
import net.ymate.platform.webmvc.IUploadFileWrapper;

import java.io.File;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description:
 */
public final class MxUpload implements IModule, IMxUpload {


    private static volatile IMxUpload instance;

    private IApplication owner;

    private IMxUploadConfig config;

    private boolean initialized;


    public static IMxUpload get() {
        IMxUpload inst = instance;
        if (inst == null) {
            synchronized (MxUpload.class) {
                inst = instance;
                if (inst == null) {
                    instance = inst = YMP.get().getModuleManager().getModule(MxUpload.class);
                }
            }
        }
        return inst;
    }

    public MxUpload() {
    }

    public MxUpload(IMxUploadConfig config) {
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
                        config = DefaultMxUploadConfig.create(moduleConfigurer);
                    } else {
                        config = DefaultMxUploadConfig.create(DefaultModuleConfigurer.createEmpty(MODULE_NAME));
                    }
                }
            }
            initialized = true;
            YMP.showVersion("初始化 mx-ymate-mxUpload-${version} 模块成功", new Version(1, 0, 0, MxUpload.class, Version.VersionType.Release));
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
    public IMxUploadConfig getConfig() {
        return config;
    }

    @Override
    public MxResult upload(File file) throws Exception {
        return config.adapter().upload(file);
    }

    @Override
    public MxResult upload(IUploadFileWrapper file) throws Exception {
        return upload(file.getFile());
    }


}
