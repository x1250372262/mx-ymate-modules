package com.mx.ymate.security.web.controller;

import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.base.annotation.NoLogin;
import com.mx.ymate.security.web.service.ISecurityScanLoginService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;


/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:03
 * @Description:
 */
@Controller
@RequestMapping("/scan/login")
@NoLogin
public class SecurityScanLoginController {

    @Inject
    private ISecurityScanLoginService iSecurityScanLoginService;


    /**
     * 生成登录二维码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/generate/qrcode", method = Type.HttpMethod.POST)
    public IView generateQrcode() throws Exception {
        return iSecurityScanLoginService.generateQrcode().toJsonView();
    }

    /**
     * 轮询检查二维码
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/qrcode", method = Type.HttpMethod.POST)
    public IView checkQrcode(@VRequired(msg = "二维码登录标识不能为空")
                             @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.checkQrcode(loginKey).toJsonView();
    }

    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/", method = Type.HttpMethod.POST)
    public IView login(@VRequired(msg = "登录id不能为空")
                       @RequestParam String loginId,
                       @VRequired(msg = "二维码登录标识不能为空")
                       @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.login(loginId, loginKey).toJsonView();
    }


    /**
     * 扫码
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    @RequestMapping("/scan")
    public IView scan(@VRequired(msg = "loginKey不能为空") @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.scan(loginKey).toJsonView();
    }

    /**
     * 扫码之后登录
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    public IView login(@VRequired(msg = "loginKey不能为空") @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.login(SaUtil.loginId(), loginKey).toJsonView();
    }

    /**
     * 扫码之后取消登录
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    @RequestMapping("/cancel/login")
    public IView cancelLogin(@VRequired(msg = "loginKey不能为空") @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.cancelLogin(loginKey).toJsonView();
    }
}
