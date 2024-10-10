package com.mx.ymate.sms.adapter.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.sms.adapter.ISmsAdapter;
import net.ymate.platform.log.Logs;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.mx.ymate.sms.code.SmsCode.*;

/**
 * @Author: xujianpeng.
 * @Date 2024/10/10.
 * @Time: 09:53.
 * @Description:
 */
public class AliSmsAdapter implements ISmsAdapter {

    private String secretId;

    private String secretKey;

    private String aliSign;

    private String aliTemplateCode;

    private String aliEndpoint;

    private Client client;

    public AliSmsAdapter() {
    }

    public AliSmsAdapter(String secretId, String secretKey, String aliSign, String aliTemplateCode, String aliEndpoint) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(secretKey)
                || StringUtils.isBlank(aliSign) || StringUtils.isBlank(aliTemplateCode) || StringUtils.isBlank(aliEndpoint)) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.aliSign = aliSign;
        this.aliTemplateCode = aliTemplateCode;
        this.aliEndpoint = aliEndpoint;
        Config config = new Config()
                .setAccessKeyId(secretId)
                .setAccessKeySecret(secretKey)
                .setEndpoint(aliEndpoint);
        try {
            client = new Client(config);
        } catch (Exception e) {
            Logs.get().getLogger().error("阿里云短信初始化失败", e);
            throw new RuntimeException(e);
        }
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
        List<String> signList = new ArrayList<>(Collections.nCopies(mobileList.size(), aliSign));
        List<String> templateParamList = new ArrayList<>(Collections.nCopies(mobileList.size(), (String) params));
        SendBatchSmsRequest sendReq = new SendBatchSmsRequest()
                .setPhoneNumberJson(JSON.toJSONString(mobileList))
                .setSignNameJson(JSON.toJSONString(signList))
                .setTemplateCode(aliTemplateCode)
                .setTemplateParamJson(JSON.toJSONString(templateParamList));
        SendBatchSmsResponse sendResp = client.sendBatchSms(sendReq);
        String code = sendResp.body.code;
        if (StringUtils.isBlank(code)) {
            return MxResult.create(SEND_ERROR);
        }
        if (Objects.equals(code.toUpperCase(), RESULT_SUCCESS)) {
            return MxResult.ok();
        }
        return MxResult.create(SEND_ERROR).msg(sendResp.body.message);
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

    public String getAliSign() {
        return aliSign;
    }

    public void setAliSign(String aliSign) {
        this.aliSign = aliSign;
    }

    public String getAliTemplateCode() {
        return aliTemplateCode;
    }

    public void setAliTemplateCode(String aliTemplateCode) {
        this.aliTemplateCode = aliTemplateCode;
    }

    public String getAliEndpoint() {
        return aliEndpoint;
    }

    public void setAliEndpoint(String aliEndpoint) {
        this.aliEndpoint = aliEndpoint;
    }


}
