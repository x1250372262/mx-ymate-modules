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

import java.util.*;

import static com.mx.ymate.sms.code.SmsCode.*;

/**
 * @Author: mengxiang.
 * @Date 2024/10/10.
 * @Time: 09:53.
 * @Description:
 */
public class AliSmsAdapter implements ISmsAdapter {


    private String aliSign;


    private Client client;

    private Map<String, String> templateIdMap;

    public AliSmsAdapter() {
    }

    public AliSmsAdapter(String secretId, String secretKey, String aliSign, String aliTemplateCode, String aliEndpoint) {
        if (StringUtils.isBlank(secretKey) || StringUtils.isBlank(secretKey)
                || StringUtils.isBlank(aliSign) || StringUtils.isBlank(aliTemplateCode) || StringUtils.isBlank(aliEndpoint)) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        String[] templateIds = StringUtils.split(aliTemplateCode, "|");
        if (templateIds == null || templateIds.length == 0) {
            throw new RuntimeException(CHANEL_ERROR.msg());
        }
        this.aliSign = aliSign;
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
        List<String> signList = new ArrayList<>(Collections.nCopies(mobileList.size(), aliSign));
        List<String> templateParamList = new ArrayList<>(Collections.nCopies(mobileList.size(), (String) params));
        SendBatchSmsRequest sendReq = new SendBatchSmsRequest()
                .setPhoneNumberJson(JSON.toJSONString(mobileList))
                .setSignNameJson(JSON.toJSONString(signList))
                .setTemplateCode(templateId)
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

    @Override
    public MxResult send(String mobile, Object params) throws Exception {
        return send(mobile, null, params);
    }

    @Override
    public MxResult send(List<String> mobileList, Object params) throws Exception {
        return send(mobileList, null, params);
    }


}
