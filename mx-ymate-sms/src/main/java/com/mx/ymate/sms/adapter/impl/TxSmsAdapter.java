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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mx.ymate.sms.code.SmsCode.*;

/**
 * @Author: mengxiang.
 * @Date 2024/10/10.
 * @Time: 09:53.
 * @Description:
 */
public class TxSmsAdapter implements ISmsAdapter {

    private String txAppId;

    private String txSignName;

    private SmsClient client;

    private Map<String, String> templateIdMap;


    public TxSmsAdapter() {
    }

    public TxSmsAdapter(String secretId, String secretKey, String txAppId, String txRegion, String txSignName, String txTemplateId) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(secretKey)
                || StringUtils.isBlank(txAppId) || StringUtils.isBlank(txRegion)
                || StringUtils.isBlank(txSignName) || StringUtils.isBlank(txTemplateId)) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        String[] templateIds = StringUtils.split(txTemplateId, "|");
        if (templateIds == null || templateIds.length == 0) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        this.txAppId = txAppId;
        this.txSignName = txSignName;
        templateIdMap = new HashMap<>();
        for (String templateId : templateIds) {
            String[] arr = StringUtils.split(templateId, "=");
            if (arr == null || arr.length == 0) {
                continue;
            }
            if (arr.length == 2) {
                templateIdMap.put(arr[0], arr[1]);
            } else {
                templateIdMap.put(MX_TEMPLATE_ID, templateId);
            }
        }
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        Credential credential = new Credential(secretId, secretKey);
        client = new SmsClient(credential, txRegion, clientProfile);
    }

    @Override
    public MxResult send(String mobile, String templateKey, Object params) throws Exception {
        if (params == null || StringUtils.isBlank(mobile)) {
            return MxResult.create(PARAMS_NOT_EMPTY);
        }
        return send(ListUtil.toList(mobile), templateKey, params);
    }

    @Override
    public MxResult send(List<String> mobileList, String templateKey, Object params) throws Exception {
        if (params == null || CollUtil.isEmpty(mobileList)) {
            return MxResult.create(PARAMS_NOT_EMPTY);
        }
        if (StringUtils.isBlank(templateKey)) {
            templateKey = MX_TEMPLATE_ID;
        }
        String templateId = templateIdMap.get(templateKey);
        if (StringUtils.isBlank(templateId)) {
            return MxResult.create(TEMPLATE_ID_ERROR);
        }
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setSmsSdkAppid(txAppId);
        smsRequest.setSign(txSignName);
        smsRequest.setTemplateID(templateId);
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


    @Override
    public MxResult send(String mobile, Object params) throws Exception {
        return send(mobile,null,params);
    }

    @Override
    public MxResult send(List<String> mobileList, Object params) throws Exception {
        return send(mobileList,null,params);
    }
}
