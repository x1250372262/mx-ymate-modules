package com.mx.ymate.security.web.controller;

import com.mx.ymate.security.web.service.ISecurityPermissionService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.view.IView;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Controller
@RequestMapping("/permission")
public class SecurityPermissionController {

    @Inject
    private ISecurityPermissionService iPermissionService;


    /**
     * 权限列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/select")
    public IView select() throws Exception {
        return iPermissionService.select().toJsonView();
    }

}
