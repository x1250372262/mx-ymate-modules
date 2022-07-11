package com.mx.ymate.security.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.mx.ymate.dev.result.MxResult;
import com.mx.ymate.satoken.annotation.NoLogin;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.dto.SecurityLoginInfoDTO;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.service.ISecurityLoginService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.annotation.VModel;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.ModelBind;
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
    @OperationLog(operationType = OperationType.LOGIN, title = "管理员登录")
    public IView login(@VRequired(msg = "用户名不能为空")
                       @RequestParam String userName,
                       @VRequired(msg = "密码不能为空")
                       @RequestParam String password) throws Exception {
        return iSecurityLoginService.login(userName, password).toMxJsonView();
    }

    /**
     * 管理员退出
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logout", method = Type.HttpMethod.POST)
    @NoLogin
    @OperationLog(operationType = OperationType.LOGIN, title = "管理员退出")
    public IView logout() throws Exception {
        return iSecurityLoginService.logout().toMxJsonView();
    }

    /**
     * 管理员信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/info", method = Type.HttpMethod.GET)
    @SaCheckLogin
    public IView info() throws Exception {
        return MxResult.ok().data(iSecurityLoginService.info()).toMxJsonView();
    }

    /**
     * 修改管理员信息
     *
     * @param securityLoginInfoDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = Type.HttpMethod.POST)
    @OperationLog(operationType = OperationType.UPDATE, title = "修改管理员信息")
    @SaCheckLogin
    public IView update(@VModel @ModelBind SecurityLoginInfoDTO securityLoginInfoDTO) throws Exception {
        return iSecurityLoginService.update(securityLoginInfoDTO.toBean()).toMxJsonView();
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
    @OperationLog(operationType = OperationType.UPDATE, title = "修改管理员密码")
    @SaCheckLogin
    public IView password(@VRequired(msg = "旧密码不能为空")
                          @RequestParam String oldPassword,
                          @VRequired(msg = "新密码不能为空")
                          @RequestParam String newPassword,
                          @VRequired(msg = "确认密码不能为空")
                          @RequestParam String rePassword) throws Exception {
        return iSecurityLoginService.password(oldPassword, newPassword, rePassword).toMxJsonView();
    }


}
