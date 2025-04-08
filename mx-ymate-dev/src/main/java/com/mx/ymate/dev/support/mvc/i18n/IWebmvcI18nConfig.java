package com.mx.ymate.dev.support.mvc.i18n;

import java.util.Map;

/**
 * @Author: xujianpeng.
 * @Date 2025/4/8.
 * @Time: 10:46.
 * @Description:
 */
public interface IWebmvcI18nConfig {

    /**
     * 获取配置
     *
     * @return
     */
    Map<String, Map<String, String>> getConfig();
}
