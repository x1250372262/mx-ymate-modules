package com.mx.ymate.security.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.service.ISecurityOperationLogService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;


/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:03
 * @Description:
 */
@Controller
@RequestMapping("/log")
public class SecurityOperationLogController {

    @Inject
    private ISecurityOperationLogService iLogService;


    /**
     * 日志列表
     *
     * @param title
     * @param startTime
     * @param endTime
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_LIST)
    public IView list(@RequestParam String title,
                      @RequestParam Long startTime,
                      @RequestParam Long endTime,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iLogService.list(title, startTime, endTime, pageDTO.toBean()).toMxJsonView();
    }


    /**
     * 日志详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_DETAIL)
    public IView detail(@PathVariable String id) throws Exception {
        return iLogService.detail(id).toMxJsonView();
    }

    /**
     * 删除日志
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_DELETE)
    @OperationLog(operationType = OperationType.DELETE, title = "删除日志")
    public IView delete(@VRequired(msg = "ids不能为空")
                        @RequestParam("ids[]") String[] ids) throws Exception {
        return iLogService.delete(ids).toMxJsonView();
    }
}
