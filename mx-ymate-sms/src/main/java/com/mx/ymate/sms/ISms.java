/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.ymate.sms;

import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.core.IApplication;
import net.ymate.platform.core.beans.annotation.Ignored;
import net.ymate.platform.core.support.IDestroyable;
import net.ymate.platform.core.support.IInitialization;

import java.util.List;

/**
 * @Author: mengxiang.
 * @Date 2024/10/9.
 * @Time: 09:36.
 * @Description:
 */
@Ignored
public interface ISms extends IInitialization<IApplication>, IDestroyable {

    String MODULE_NAME = "module.sms";

    /**
     * 获取所属应用容器
     *
     * @return 返回所属应用容器实例
     */
    IApplication getOwner();

    /**
     * 获取配置
     *
     * @return 返回配置对象
     */
    ISmsConfig getConfig();


    /**
     * 发送短信
     *
     * @param channel 通道
     * @param mobile  手机号
     * @param params  参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult sendByChannel(String channel, String mobile, Object params) throws Exception;


    /**
     * 批量发送短信 内容一样  内容不一样可以循环发送
     *
     * @param channel    通道
     * @param mobileList 手机号
     * @param params     参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult sendByChannel(String channel, List<String> mobileList, Object params) throws Exception;

    /**
     * 发送短信
     *
     * @param channel     通道
     * @param mobile      手机号
     * @param templateKey 模板idKey
     * @param params      参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult sendByChannel(String channel, String mobile, String templateKey, Object params) throws Exception;


    /**
     * 批量发送短信 内容一样  内容不一样可以循环发送
     *
     * @param channel     通道
     * @param mobileList  手机号
     * @param templateKey 模板idKey
     * @param params      参数 注意：在不同服务商内容不同 中国网建：短信内容 阿里：json字符串方式参数 腾讯：数组方式参数
     * @return
     * @throws Exception
     */
    MxResult sendByChannel(String channel, List<String> mobileList, String templateKey, Object params) throws Exception;


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


}
