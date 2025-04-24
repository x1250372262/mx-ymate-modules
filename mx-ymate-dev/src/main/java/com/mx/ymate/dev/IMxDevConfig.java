package com.mx.ymate.dev;

import com.mx.ymate.dev.support.mvc.i18n.IWebmvcI18nConfig;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IInitialization;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description:
 */
@Ignored
public interface IMxDevConfig extends IInitialization<IMxDev> {

    String ENABLED = "enabled";

    String QRCODE_FILE_PATH = "qrcode.file_path";

    String QRCODE_WEB_URL = "qrcode.web_url";

    String INITIALIZER_PACKAGES = "initializer.packages";

    String I18N_ENABLED = "i18n.enabled";

    String I18N_CONFIG_CLASS = "i18n.config_class";

    String I18N_DEFAULT_LANGUAGE = "i18n.default_language";

    String I18N_HEADER_LANGUAGE = "i18n.header_language";

    String I18N_OPEN_FALLBACK = "i18n.open_fallback";



    /**
     * 模块是否已启用, 默认值: true
     *
     * @return 返回false表示禁用
     */
    boolean isEnabled();

    /**
     * 二维码生成路径 不可为空
     *
     * @return
     */
    String qrcodeFilePath();

    /**
     * 二维码网络地址 不填默认为系统生成
     *
     * @return
     */
    String qrcodeWebUrl();

    /**
     * 启动事件自动扫描包 用|分割
     *
     * @return
     */
    String initializerPackages();

    /**
     * 是否开启i18n 默认false
     * @return
     */
    boolean i18nEnabled();

    /**
     * i18n配置类
     *
     * @return
     */
    IWebmvcI18nConfig i18nConfigClass();


    /**
     * 默认语言名称 默认zh
     *
     * @return
     */
    String i18nDefaultLanguage();


    /**
     * 请求头语言key 默认language
     *
     * @return
     */
    String i18nHeaderLanguage();

    /**
     * 是否启用fallback 其他语言获取不到 是否获取默认语言的 默认false
     * @return
     */
    boolean i18nOpenFallback();
}