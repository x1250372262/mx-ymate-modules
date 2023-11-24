package com.mx.ymate.security.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.web.service.ISecurityOperationLogService;
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
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_LIST)
    public IView list(@RequestParam String title,
                      @RequestParam Long startTime,
                      @RequestParam Long endTime,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iLogService.list(title, startTime, endTime, pageDTO.toBean()).toJsonView();
    }


    /**
     * 日志详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_DETAIL)
    public IView detail(@PathVariable String id) throws Exception {
        return iLogService.detail(id).toJsonView();
    }

    /**
     * 删除日志
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_LOG_DELETE)
    public IView delete(@VRequired(msg = "ids不能为空")
                        @RequestParam("ids[]") String[] ids) throws Exception {
        return iLogService.delete(ids).toJsonView();
    }
}
