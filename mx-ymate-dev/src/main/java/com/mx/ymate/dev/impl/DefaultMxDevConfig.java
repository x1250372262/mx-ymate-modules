package com.mx.ymate.dev.impl;

import com.mx.ymate.dev.IMxDev;
import com.mx.ymate.dev.IMxDevConfig;
import com.mx.ymate.dev.support.mvc.i18n.IWebmvcI18nConfig;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.configuration.IConfigReader;
import net.ymate.platform.core.module.IModuleConfigurer;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public final class DefaultMxDevConfig implements IMxDevConfig {

    private boolean enabled;

    /**
     * 二维码生成路径
     */
    private String qrcodeFilePath;

    /**
     * 二维码网络地址
     */
    private String qrcodeWebUrl;

    /**
     * 启动事件自动扫描包
     */
    private String initializerPackages;

    /**
     * 是否开启i18n
     */
    private boolean i18nEnabled;

    /**
     * i18n配置类
     */
    private IWebmvcI18nConfig i18nConfigClass;

    /**
     * 默认语言名称
     */
    private String i18nDefaultLanguage;

    /**
     * 请求头语言key
     */
    private String i18nHeaderLanguage;

    /**
     * 是否启用fallback 其他语言获取不到 是否获取默认语言的
     */
    private boolean i18nOpenFallback;

    private boolean initialized;

    public static DefaultMxDevConfig create(IModuleConfigurer moduleConfigurer) {
        return new DefaultMxDevConfig(moduleConfigurer);
    }

    private DefaultMxDevConfig() {
    }


    private DefaultMxDevConfig(IModuleConfigurer moduleConfigurer) {
        IConfigReader configReader = moduleConfigurer.getConfigReader();
        enabled = configReader.getBoolean(ENABLED, true);
        qrcodeFilePath = configReader.getString(QRCODE_FILE_PATH);
        qrcodeWebUrl = configReader.getString(QRCODE_WEB_URL);
        initializerPackages = configReader.getString(INITIALIZER_PACKAGES);
        i18nEnabled = configReader.getBoolean(I18N_ENABLED, false);
        String i18nConfigClassName = configReader.getString(I18N_CONFIG_CLASS);
        if (StringUtils.isNotBlank(i18nConfigClassName)) {
            i18nConfigClass = ClassUtils.impl(i18nConfigClassName, IWebmvcI18nConfig.class, this.getClass());
        }
        i18nDefaultLanguage = configReader.getString(I18N_DEFAULT_LANGUAGE,"zh");
        i18nHeaderLanguage = configReader.getString(I18N_HEADER_LANGUAGE,"language");
        i18nOpenFallback = configReader.getBoolean(I18N_OPEN_FALLBACK, false);
    }

    @Override
    public void initialize(IMxDev owner) throws Exception {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String qrcodeFilePath() {
        return qrcodeFilePath;
    }

    @Override
    public String qrcodeWebUrl() {
        return qrcodeWebUrl;
    }

    @Override
    public String initializerPackages() {
        return initializerPackages;
    }

    @Override
    public boolean i18nEnabled() {
        return i18nEnabled;
    }

    @Override
    public IWebmvcI18nConfig i18nConfigClass() {
        return i18nConfigClass;
    }

    @Override
    public String i18nDefaultLanguage() {
        return i18nDefaultLanguage;
    }

    @Override
    public String i18nHeaderLanguage() {
        return i18nHeaderLanguage;
    }

    @Override
    public boolean i18nOpenFallback() {
        return i18nOpenFallback;
    }


}