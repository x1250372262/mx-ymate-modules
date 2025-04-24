package com.mx.ymate.security.web.service;


import com.mx.ymate.dev.support.mvc.MxResult;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public interface ISecurityScanLoginService {


    /**
     * 生成登录二维码
     *
     * @return
     * @throws Exception
     */
    MxResult generateQrcode() throws Exception;

    /**
     * 检查二维码状态
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    MxResult checkQrcode(String loginKey) throws Exception;

    /**
     * 扫码方法
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    MxResult scan(String loginKey) throws Exception;

    /**
     * 登录
     *
     * @param loginId
     * @param loginKey
     * @return
     * @throws Exception
     */
    MxResult login(String loginId, String loginKey) throws Exception;

    /**
     * 扫码之后取消登录
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    MxResult cancelLogin(String loginKey) throws Exception;
}
