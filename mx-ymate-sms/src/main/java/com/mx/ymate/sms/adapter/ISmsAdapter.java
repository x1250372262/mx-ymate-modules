package com.mx.ymate.sms.adapter;

import com.mx.ymate.dev.support.mvc.MxResult;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 10:19.
 * @Description:
 */
public interface ISmsAdapter {

    String RESULT_SUCCESS = "OK";
    String MX_TEMPLATE_ID = "mxTemplateId";

    /**
     * 发送短信
     *
     * @param mobile      手机号
     * @param templateKey 模板idKey
     * @param params      参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult send(String mobile, String templateKey, Object params) throws Exception;

    /**
     * 批量发送短信 内容一样  内容不一样可以循环发送
     *
     * @param mobileList  手机号
     * @param templateKey 模板idKey
     * @param params      参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult send(List<String> mobileList, String templateKey, Object params) throws Exception;

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param params 参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult send(String mobile, Object params) throws Exception;


    /**
     * 批量发送短信 内容一样  内容不一样可以循环发送
     *
     * @param mobileList 手机号
     * @param params     参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult send(List<String> mobileList, Object params) throws Exception;


}
