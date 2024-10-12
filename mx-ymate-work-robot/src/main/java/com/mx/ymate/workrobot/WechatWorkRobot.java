package com.mx.ymate.workrobot;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.workrobot.bean.WechatWorkResult;
import com.mx.ymate.workrobot.bean.WechatWorkRobotMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date: 2023-06-09 15:09
 * @Description:
 */
public class WechatWorkRobot {

    public static WechatWorkResult send(String url, WechatWorkRobotMessage wechatWorkRobotMessage) {
        Map<String, String> headerParams = new HashMap<>(2);
        headerParams.put("Content-Type", "application/json");
        headerParams.put("charset", "utf-8");
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .addHeaders(headerParams)
                .body(JSONObject.toJSONString(wechatWorkRobotMessage)).execute();
        String responseStr = httpResponse.body();
        if (StringUtils.isBlank(responseStr)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        if (jsonObject == null) {
            return null;
        }
        return WechatWorkResult.create(jsonObject);
    }
}
