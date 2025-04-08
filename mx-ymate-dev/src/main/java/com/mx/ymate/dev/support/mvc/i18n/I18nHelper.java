package com.mx.ymate.dev.support.mvc.i18n;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: xujianpeng.
 * @Date 2025/4/8.
 * @Time: 10:48.
 * @Description:
 */
public class I18nHelper {

    /**
     * I18N_MAP 的结构是：语言 ➜ (key ➜ value)
     */
    private static final Map<String, Map<String, String>> I18N_MAP = new ConcurrentHashMap<>();

    /**
     * 默认语言
     */
    private static final String DEFAULT_LANG = "zh";

    /**
     * 是否初始化
     */
    private static boolean isInit = false;

    /**
     * 请求头语言
     */
    private static String headerLanguage = "";

    /**
     * 初始化
     */
    public static void init() {
        String webmvcI18nConfigName = YMP.get().getParam("mx.webmvc.i18n.config_class");
        if (StringUtils.isBlank(webmvcI18nConfigName)) {
            return;
        }
        IWebmvcI18nConfig iWebmvcI18nConfig = ClassUtils.impl(webmvcI18nConfigName, IWebmvcI18nConfig.class, I18nHelper.class);
        if (iWebmvcI18nConfig == null) {
            return;
        }
        addAll(iWebmvcI18nConfig.getConfig());
        isInit = true;
    }

    /**
     * 添加一整个语言的数据（覆盖式）
     */
    public static void addLanguage(String language, Map<String, String> keyValues) {
        I18N_MAP.put(language, new ConcurrentHashMap<>(keyValues));
    }

    /**
     * 添加单条 key-value 到指定语言中
     */
    public static void add(String language, String key, String value) {
        I18N_MAP.computeIfAbsent(language, lang -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    /**
     * 添加所有语言的数据（批量导入）
     * map 的结构是：语言 ➜ (key ➜ value)
     */
    public static void addAll(Map<String, Map<String, String>> i18nMap) {
        for (Map.Entry<String, Map<String, String>> entry : i18nMap.entrySet()) {
            addLanguage(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取翻译的值
     */
    public static String get(String language, String key) {
        Map<String, String> langMap = I18N_MAP.get(language);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        // fallback
        Map<String, String> defaultMap = I18N_MAP.get(DEFAULT_LANG);
        if (defaultMap != null && defaultMap.containsKey(key)) {
            return defaultMap.get(key);
        }
        return "无效的key:" + key;
    }

    /**
     * 判断是否包含某个 key 的翻译
     */
    public static boolean contains(String language, String key) {
        Map<String, String> langMap = I18N_MAP.get(language);
        return langMap != null && langMap.containsKey(key);
    }

    /**
     * 获取当前支持的语言
     */
    public static Set<String> getLanguages() {
        return I18N_MAP.keySet();
    }

    /**
     * 获取某语言下的所有 key
     */
    public static Set<String> getKeys(String language) {
        return Optional.ofNullable(I18N_MAP.get(language))
                .map(Map::keySet)
                .orElse(Collections.emptySet());
    }

    public static String getMsg(String key) {
        if (!isInit) {
            return key;
        }
        if(StringUtils.isBlank(headerLanguage)){
            headerLanguage = StringUtils.defaultIfBlank(WebContext.getRequest().getHeader("language"), DEFAULT_LANG);
        }
        return get(headerLanguage, key);
    }
}
