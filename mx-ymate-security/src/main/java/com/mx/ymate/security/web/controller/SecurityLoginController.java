package com.mx.ymate.security.web.controller;

import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.security.base.annotation.NoLogin;
import com.mx.ymate.security.base.dto.SecurityLoginInfoDTO;
import com.mx.ymate.security.web.service.ISecurityLoginService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.annotation.VModel;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.ModelBind;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

import static com.mx.ymate.security.ValidateConstant.*;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Controller
@RequestMapping("/login")
public class SecurityLoginController {

    @Inject
    private ISecurityLoginService iSecurityLoginService;

    /**
     * 管理员登录
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = Type.HttpMethod.POST)
    @NoLogin
    public IView login(@VMxRequired(msg = USER_NAME_NOT_EMPTY_MSG, i18nKey = USER_NAME_NOT_EMPTY_I18N_KEY)
                       @RequestParam String userName,
                       @VMxRequired(msg = PASSWORD_NOT_EMPTY_MSG, i18nKey = PASSWORD_NOT_EMPTY_I18N_KEY)
                       @RequestParam String password) throws Exception {
        return iSecurityLoginService.login(userName, password).toJsonView();
    }

    /**
     * 管理员解锁
     *
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unlock", method = Type.HttpMethod.POST)
    public IView unlock(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                        @RequestParam String id,
                        @VMxRequired(msg = PASSWORD_NOT_EMPTY_MSG, i18nKey = PASSWORD_NOT_EMPTY_I18N_KEY)
                        @RequestParam String password) throws Exception {
        return iSecurityLoginService.unlock(id, password).toJsonView();
    }

    /**
     * 管理员锁定
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/lock", method = Type.HttpMethod.POST)
    public IView lock(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                      @RequestParam String id) throws Exception {
        return iSecurityLoginService.lock(id).toJsonView();
    }

    /**
     * 检查是否加锁
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/check/lock", method = Type.HttpMethod.POST)
    public IView checkLock(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                           @RequestParam String id) throws Exception {
        return iSecurityLoginService.checkLock(id).toJsonView();
    }

    /**
     * 管理员退出
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout", method = Type.HttpMethod.POST)
    @NoLogin
    public IView logout() throws Exception {
        return iSecurityLoginService.logout().toJsonView();
    }

    /**
     * 管理员信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/info", method = Type.HttpMethod.GET)
    public IView info() throws Exception {
        return MxResult.ok().data(iSecurityLoginService.info()).toJsonView();
    }

    /**
     * 修改管理员信息
     *
     * @param securityLoginInfoDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = Type.HttpMethod.POST)
    public IView update(@VModel @ModelBind SecurityLoginInfoDTO securityLoginInfoDTO) throws Exception {
        return iSecurityLoginService.update(securityLoginInfoDTO.toBean()).toJsonView();
    }


    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @param rePassword
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/password", method = Type.HttpMethod.POST)
    public IView password(@VMxRequired(msg = OLD_PASSWORD_NOT_EMPTY_MSG, i18nKey = OLD_PASSWORD_NOT_EMPTY_I18N_KEY)
                          @RequestParam String oldPassword,
                          @VMxRequired(msg = NEW_PASSWORD_NOT_EMPTY_MSG, i18nKey = NEW_PASSWORD_NOT_EMPTY_I18N_KEY)
                          @RequestParam String newPassword,
                          @VMxRequired(msg = RE_PASSWORD_NOT_EMPTY_MSG, i18nKey = RE_PASSWORD_NOT_EMPTY_I18N_KEY)
                          @RequestParam String rePassword) throws Exception {
        return iSecurityLoginService.password(oldPassword, newPassword, rePassword).toJsonView();
    }


}
