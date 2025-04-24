package com.mx.ymate.security.web.controller;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.security.base.annotation.NoLogin;
import com.mx.ymate.security.web.service.ISecurityScanLoginService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

import static com.mx.ymate.security.I18nConstant.*;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
    public IView checkQrcode(@VMxRequired(msg = SCAN_QR_CODE_FLAG_NOT_EMPTY_MSG, i18nKey = SCAN_QR_CODE_FLAG_NOT_EMPTY_I18N_KEY)
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
    public IView login(@VMxRequired(msg = SCAN_LOGIN_ID_NOT_EMPTY_MSG, i18nKey = SCAN_LOGIN_ID_NOT_EMPTY_I18N_KEY)
                       @RequestParam String loginId,
                       @VMxRequired(msg = SCAN_QR_CODE_FLAG_NOT_EMPTY_MSG, i18nKey = SCAN_QR_CODE_FLAG_NOT_EMPTY_I18N_KEY)
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
    @RequestMapping(value = "/scan", method = Type.HttpMethod.POST)
    public IView scan(@VMxRequired(msg = SCAN_LOGIN_KEY_NOT_EMPTY_MSG, i18nKey = SCAN_LOGIN_KEY_NOT_EMPTY_I18N_KEY)
                      @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.scan(loginKey).toJsonView();
    }


    /**
     * 扫码之后取消登录
     *
     * @param loginKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancel/login", method = Type.HttpMethod.POST)
    public IView cancelLogin(@VMxRequired(msg = SCAN_LOGIN_KEY_NOT_EMPTY_MSG, i18nKey = SCAN_LOGIN_KEY_NOT_EMPTY_I18N_KEY)
                             @RequestParam String loginKey) throws Exception {
        return iSecurityScanLoginService.cancelLogin(loginKey).toJsonView();
    }
}
