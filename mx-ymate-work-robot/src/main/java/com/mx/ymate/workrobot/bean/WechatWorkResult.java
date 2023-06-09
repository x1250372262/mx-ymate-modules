package com.mx.ymate.workrobot.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: xujianpeng.
 * @Date: 2023-06-09 15:26
 * @Description:
 */
public class WechatWorkResult {

    private String errCode;

    private String errMessage;

    private WechatWorkResult() {

    }

    public WechatWorkResult(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public static WechatWorkResult create(JSONObject jsonObject) {
        return new WechatWorkResult(jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
    }

    public boolean isSuccess() {
        return "0".equals(this.errCode) && "ok".equals(this.errMessage);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
