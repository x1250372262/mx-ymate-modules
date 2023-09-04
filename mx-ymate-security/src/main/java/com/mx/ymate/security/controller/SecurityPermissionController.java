package com.mx.ymate.security.controller;

import com.mx.ymate.security.service.ISecurityPermissionService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.view.IView;


/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:03
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
    public IView list() throws Exception {
        return iPermissionService.list().toJsonView();
    }

}
