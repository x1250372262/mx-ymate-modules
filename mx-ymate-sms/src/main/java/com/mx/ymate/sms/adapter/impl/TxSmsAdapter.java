package com.mx.ymate.sms.adapter.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.sms.adapter.ISmsAdapter;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.mx.ymate.sms.code.SmsCode.*;

/**
 * @Author: xujianpeng.
 * @Date 2024/10/10.
 * @Time: 09:53.
 * @Description:
 */
public class TxSmsAdapter implements ISmsAdapter {

    private String secretId;

    private String secretKey;

    private String txAppId;

    private String txRegion;

    private String txSignName;

    private String txTemplateId;

    private SmsClient client;


    public TxSmsAdapter() {
    }

    public TxSmsAdapter(String secretId, String secretKey, String txAppId, String txRegion, String txSignName, String txTemplateId) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(secretKey)
                || StringUtils.isBlank(txAppId) || StringUtils.isBlank(txRegion)
                || StringUtils.isBlank(txSignName) || StringUtils.isBlank(txTemplateId)) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.txAppId = txAppId;
        this.txRegion = txRegion;
        this.txSignName = txSignName;
        this.txTemplateId = txTemplateId;
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        Credential credential = new Credential(secretId, secretKey);
        client = new SmsClient(credential, txRegion, clientProfile);
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
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setSmsSdkAppid(txAppId);
        smsRequest.setSign(txSignName);
        smsRequest.setTemplateID(txTemplateId);
        smsRequest.setTemplateParamSet((String[]) params);
        String[] mobileArr = mobileList.toArray(new String[0]);
        smsRequest.setPhoneNumberSet(mobileArr);
        SendSmsResponse res = client.SendSms(smsRequest);
        SendStatus sendStatus = res.getSendStatusSet()[0];
        String code = sendStatus.getCode();
        if (StringUtils.isBlank(code)) {
            return MxResult.create(SEND_ERROR);
        }
        if (Objects.equals(code.toUpperCase(), RESULT_SUCCESS)) {
            return MxResult.ok();
        }
        return MxResult.create(SEND_ERROR).msg(sendStatus.getMessage());
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

    public String getTxAppId() {
        return txAppId;
    }

    public void setTxAppId(String txAppId) {
        this.txAppId = txAppId;
    }

    public String getTxRegion() {
        return txRegion;
    }

    public void setTxRegion(String txRegion) {
        this.txRegion = txRegion;
    }

    public String getTxSignName() {
        return txSignName;
    }

    public void setTxSignName(String txSignName) {
        this.txSignName = txSignName;
    }

    public String getTxTemplateId() {
        return txTemplateId;
    }

    public void setTxTemplateId(String txTemplateId) {
        this.txTemplateId = txTemplateId;
    }


}
