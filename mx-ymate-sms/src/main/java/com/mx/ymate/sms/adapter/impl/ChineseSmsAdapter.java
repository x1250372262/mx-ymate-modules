package com.mx.ymate.sms.adapter.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.http.HttpUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.sms.adapter.ISmsAdapter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mx.ymate.sms.code.SmsCode.*;

/**
 * @Author: xujianpeng.
 * @Date 2024/10/10.
 * @Time: 09:53.
 * @Description:
 */
public class ChineseSmsAdapter implements ISmsAdapter {

    private static final String URL = "https://utf8api.smschinese.cn/";

    private String secretId;

    private String secretKey;

    private Map<String, Object> defaultParam;

    public ChineseSmsAdapter() {
    }

    public ChineseSmsAdapter(String secretId, String secretKey) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(secretKey)) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        defaultParam = new HashMap<>();
        defaultParam.put("Uid", secretId);
        defaultParam.put("Key", secretKey);
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    @Override
    public MxResult send(String mobile, String templateKey, Object params) throws Exception {
        return null;
    }

    @Override
    public MxResult send(List<String> mobileList, String templateKey, Object params) throws Exception {
        return null;
    }

    @Override
    public MxResult send(String mobile, Object params) throws Exception {
        if (params == null || StringUtils.isBlank(mobile)) {
            return MxResult.create(PARAMS_NOT_EMPTY);
        }
        return send(ListUtil.toList(mobile), params);
    }

    @Override
    public MxResult send(List<String> mobileList, Object params) throws Exception {
        if (params == null || CollUtil.isEmpty(mobileList)) {
            return MxResult.create(PARAMS_NOT_EMPTY);
        }
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.putAll(defaultParam);
        String mobileStr = String.join(",", mobileList);
        paramMap.put("smsMob", mobileStr);
        paramMap.put("smsText", params);
        String resultStr = HttpUtil.post(URL, paramMap);
        int result = Integer.parseInt(resultStr);
        if (result >= 1) {
            return MxResult.ok();
        }
        return MxResult.create(SEND_ERROR).msg(resultStr);
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
