package com.mx.ymate.dev.util;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class ConfigUtil {

    private final Map<String, String> configMap;


    public ConfigUtil(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    private BlurObject getObject(String key, Object defaultValue) {
        Object value = configMap.get(key);
        if (StringUtils.isBlank((CharSequence) value)) {
            value = defaultValue;
        }
        return BlurObject.bind(value);
    }


    public String getString(String key) {
        return BlurObject.bind(configMap.get(key)).toStringValue();
    }

    public String getString(String key, String defaultValue) {
        return StringUtils.defaultIfBlank(getString(key), defaultValue);
    }

    public List<String> getList(String key) {
        String[] array = getArray(key);
        if (array == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(array);
    }

    public String[] getArray(String key) {
        return StringUtils.split(getString(key), "|");
    }

    public String[] getArray(String key, String[] defaultValue) {
        String[] array = getArray(key);
        return array == null || array.length == 0 ? defaultValue : array;
    }

    public String[] getArray(String key, boolean zeroSize) {
        String[] array = StringUtils.split(getString(key), "|");
        return array == null || array.length == 0 ? (zeroSize ? new String[0] : null) : array;
    }

    public Integer getInteger(String key) {
        return BlurObject.bind(configMap.get(key)).toInteger();
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return getObject(key, defaultValue).toInteger();
    }

    public Boolean getBoolean(String key) {
        return BlurObject.bind(configMap.get(key)).toBoolean();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return getObject(key, defaultValue).toBoolean();
    }

    public Long getLong(String key) {
        return BlurObject.bind(configMap.get(key)).toLong();
    }

    public Long getLong(String key, Long defaultValue) {
        return getObject(key, defaultValue).toLong();
    }

    public Float getFloat(String key) {
        return BlurObject.bind(configMap.get(key)).toFloat();
    }

    public Float getFloat(String key, Float defaultValue) {
        return getObject(key, defaultValue).toFloat();
    }

    public Double getDouble(String key) {
        return BlurObject.bind(configMap.get(key)).toDouble();
    }

    public Double getDouble(String key, Double defaultValue) {
        return getObject(key, defaultValue).toDouble();
    }

    public int getInt(String key) {
        return BlurObject.bind(configMap.get(key)).toIntValue();
    }

    public int getInt(String key, int defaultValue) {
        return getObject(key, defaultValue).toIntValue();
    }

    public boolean getBool(String key) {
        return BlurObject.bind(configMap.get(key)).toBooleanValue();
    }

    public boolean getBool(String key, boolean defaultValue) {
        return getObject(key, defaultValue).toBooleanValue();
    }

    public long getLongValue(String key) {
        return BlurObject.bind(configMap.get(key)).toLongValue();
    }

    public long getLongValue(String key, long defaultValue) {
        return getObject(key, defaultValue).toLongValue();
    }

    public float getFloatValue(String key) {
        return BlurObject.bind(configMap.get(key)).toFloatValue();
    }

    public float getFloatValue(String key, float defaultValue) {
        return getObject(key, defaultValue).toFloatValue();
    }

    public double getDoubleValue(String key) {
        return BlurObject.bind(configMap.get(key)).toDoubleValue();
    }

    public double getDoubleValue(String key, double defaultValue) {
        return getObject(key, defaultValue).toDoubleValue();
    }


    public <T> T getClassImpl(String key, Class<T> interfaceClass) {
        return ClassUtils.impl(getString(key), interfaceClass, getClass());
    }

    public <T> T getClassImpl(String key, String defaultValue, Class<T> interfaceClass) {
        return ClassUtils.impl(getString(key, defaultValue), interfaceClass, getClass());
    }

    public boolean contains(String key) {
        return configMap.containsKey(key);
    }
}
