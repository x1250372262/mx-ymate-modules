package com.mx.ymate.captcha.sms.adapter;

import com.mx.ymate.captcha.sms.SmsBean;
import com.mx.ymate.redis.api.RedisApi;
import net.ymate.platform.commons.json.JsonWrapper;

/**
 * @Author: mengxiang.
 * @Date 2024/10/15.
 * @Time: 09:28.
 * @Description:
 */
public class RedisCacheStorageAdapter {

    public static void put(String key, SmsBean smsBean) throws Exception {
        RedisApi.strSet(key, JsonWrapper.toJsonString(smsBean));
    }

    public static SmsBean get(String key) throws Exception {
        return JsonWrapper.deserialize(RedisApi.strGet(key), SmsBean.class);
    }
}
