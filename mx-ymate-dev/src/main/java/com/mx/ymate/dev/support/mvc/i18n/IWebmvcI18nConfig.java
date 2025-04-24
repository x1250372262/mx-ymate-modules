package com.mx.ymate.dev.support.mvc.i18n;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
