package com.mx.ymate.captcha.sms;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.captcha.sms.adapter.DefaultCacheStorageAdapter;
import com.mx.ymate.captcha.sms.adapter.RedisCacheStorageAdapter;
import com.mx.ymate.captcha.sms.enums.CacheType;
import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;

import static com.mx.ymate.captcha.sms.code.SmsCaptchaCode.CODE_ERROR;
import static com.mx.ymate.captcha.sms.code.SmsCaptchaCode.EXPIRE_ERROR;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 09:36.
 * @Description:
 */
public class SmsCaptcha {

    /**
     * 默认验证码key
     */
    private final static String DEFAULT_CACHE_KEY = "mxCaptchaSms:{}";

    /**
     * 默认验证码长度
     */
    private final static int DEFAULT_CACHE_NUM = 6;

    /**
     * 验证码最小位数
     */
    private final static int MIN_CACHE_NUM = 4;


    /**
     * 创建验证码
     *
     * @param num 位数
     * @return
     */
    public static String createCode(int num) {
        if (num < MIN_CACHE_NUM) {
            num = DEFAULT_CACHE_NUM;
        }
        return UUIDUtils.randomStr(num, true);
    }

    /**
     * 缓存验证码
     *
     * @param cacheType 缓存类型
     * @param key       缓存key 有默认值
     * @param mobile    手机号
     * @param code      验证码
     * @param minutes   过期时间 单位分钟
     * @return
     */
    public static SmsBean cacheCode(CacheType cacheType, String key, String mobile, String code, long minutes) throws Exception {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
            return null;
        }
        if (StringUtils.isBlank(key)) {
            key = StrUtil.format(DEFAULT_CACHE_KEY, mobile);
        }
        SmsBean smsBean = new SmsBean(code, mobile, System.currentTimeMillis() + DateTimeUtils.MINUTE * minutes);
        if (CacheType.REDIS == cacheType) {
            RedisCacheStorageAdapter.put(key, smsBean);
        } else {
            DefaultCacheStorageAdapter.put(key, smsBean);
        }
        return smsBean;
    }

    /**
     * 检查验证码
     *
     * @param cacheType 缓存类型
     * @param key       缓存key 有默认值
     * @param mobile    手机号
     * @param code      验证码
     * @return
     * @throws Exception
     */
    public static MxResult checkCode(CacheType cacheType, String key, String mobile, String code) throws Exception {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
            return MxResult.create(CODE_ERROR);
        }
        if (StringUtils.isBlank(key)) {
            key = StrUtil.format(DEFAULT_CACHE_KEY, mobile);
        }
        SmsBean smsBean;
        if (CacheType.REDIS == cacheType) {
            smsBean = RedisCacheStorageAdapter.get(key);
        } else {
            smsBean = DefaultCacheStorageAdapter.get(key);
        }
        if (smsBean == null) {
            return MxResult.create(CODE_ERROR);
        }
        long expire = smsBean.getExpire();
        if (expire > 0 && System.currentTimeMillis() > expire) {
            return MxResult.create(EXPIRE_ERROR);
        }
        return MxResult.ok();
    }

    /**
     * 缓存验证码
     *
     * @param key     缓存key 有默认值
     * @param mobile  手机号
     * @param code    验证码
     * @param minutes 过期时间 单位分钟
     * @return
     */
    public static SmsBean cacheCode(String key, String mobile, String code, long minutes) throws Exception {
        return cacheCode(CacheType.DEFAULT, key, mobile, code, minutes);
    }

    /**
     * 检查验证码
     *
     * @param key    缓存key 有默认值
     * @param mobile 手机号
     * @param code   验证码
     * @return
     * @throws Exception
     */
    public static MxResult checkCode(String key, String mobile, String code) throws Exception {
        return checkCode(CacheType.DEFAULT, key, mobile, code);
    }

    /**
     * 缓存验证码
     *
     * @param mobile  手机号
     * @param code    验证码
     * @param minutes 过期时间 单位分钟
     * @return
     */
    public static SmsBean cacheCode(String mobile, String code, long minutes) throws Exception {
        return cacheCode(CacheType.DEFAULT, DEFAULT_CACHE_KEY, mobile, code, minutes);
    }

    /**
     * 检查验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return
     * @throws Exception
     */
    public static MxResult checkCode(String mobile, String code) throws Exception {
        return checkCode(CacheType.DEFAULT, DEFAULT_CACHE_KEY, mobile, code);
    }

}
