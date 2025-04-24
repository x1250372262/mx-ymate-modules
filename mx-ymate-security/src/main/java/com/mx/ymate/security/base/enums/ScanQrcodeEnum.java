package com.mx.ymate.security.base.enums;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
