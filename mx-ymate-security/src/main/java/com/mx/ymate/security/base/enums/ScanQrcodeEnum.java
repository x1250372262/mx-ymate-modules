package com.mx.ymate.security.base.enums;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 扫码登录二维码状态枚举
 */
public enum ScanQrcodeEnum {


    /**
     * 等待扫码
     */
    WAIT,

    /**
     * 已扫码
     */
    SCAN,

    /**
     * 登录成功
     */
    LOGIN_SUCCESS,

    /**
     * 登录失败
     */
    LOGIN_FAIL;


}
