package com.mx.ymate.dev.support.mvc.i18n;

import com.mx.ymate.dev.IMxDevConfig;
import com.mx.ymate.dev.MxDev;
import com.mx.ymate.dev.code.ICode;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 国际化帮助类
 */
public class I18nHelper {

    /**
     * I18N_MAP 的结构是：语言 ➜ (key ➜ value)
     */
    private static final Map<String, Map<String, String>> I18N_MAP = new ConcurrentHashMap<>();

    private final static IMxDevConfig MX_DEV_CONFIG = MxDev.get().getConfig();

    /**
     * 是否启用i18n
     */
    private final static boolean IS_ENABLED = MX_DEV_CONFIG.i18nEnabled();


    /**
     * 初始化
     */
    public static void init() {
        IWebmvcI18nConfig iWebmvcI18nConfig = MX_DEV_CONFIG.i18nConfigClass();
        if (iWebmvcI18nConfig == null) {
            return;
        }
        addAll(iWebmvcI18nConfig.getConfig());
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
        if(langMap == null && MX_DEV_CONFIG.i18nOpenFallback()){
            langMap = I18N_MAP.get(MX_DEV_CONFIG.i18nDefaultLanguage());
        }
        if (langMap != null) {
            return langMap.get(key);
        }
        return null;
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
        if(!IS_ENABLED){
           return null;
        }
        if(StringUtils.isBlank(key)){
            return null;
        }
        String headerLanguage = StringUtils.defaultIfBlank(WebContext.getRequest().getHeader(MX_DEV_CONFIG.i18nHeaderLanguage()),MX_DEV_CONFIG.i18nDefaultLanguage());
        return get(headerLanguage, key);
    }

    public static String getMsg(String key, String defaultValue) {
        return StringUtils.defaultIfBlank(getMsg(key), defaultValue);
    }

    public static String getMsg(ICode iCode) {
        return getMsg(iCode.i18nKey(), iCode.msg());
    }
}
