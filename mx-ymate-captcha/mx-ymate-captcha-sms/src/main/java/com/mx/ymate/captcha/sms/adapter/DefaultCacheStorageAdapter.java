package com.mx.ymate.captcha.sms.adapter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mx.ymate.captcha.sms.SmsBean;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:28.
 * @Description:
 */
public class DefaultCacheStorageAdapter {

    /**
     * 存储数据的集合
     */
    public final static Cache<String, SmsBean> DATA_MAP = Caffeine.newBuilder().build();

    public static void put(String key, SmsBean smsBean) {
        DATA_MAP.put(key, smsBean);
    }

    public static SmsBean get(String key) {
        return DATA_MAP.getIfPresent(key);
    }
}
