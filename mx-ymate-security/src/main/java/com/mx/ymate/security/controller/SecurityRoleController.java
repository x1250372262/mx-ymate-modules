package com.mx.ymate.security.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.dto.SecurityRoleDTO;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.service.ISecurityRoleService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.annotation.VModel;
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
@RequestMapping("/role")
public class SecurityRoleController {

    @Inject
    private ISecurityRoleService iRoleService;


    /**
     * 角色列表
     *
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_LIST)
    public IView list(@RequestParam String name,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iRoleService.list(name, pageDTO.toBean()).toMxJsonView();
    }

    /**
     * 添加角色
     *
     * @param roleDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_CREATE)
    @OperationLog(operationType = OperationType.CREATE, title = "添加角色")
    public IView create(@VModel @ModelBind SecurityRoleDTO roleDTO) throws Exception {
        return iRoleService.create(roleDTO.toBean()).toMxJsonView();
    }

    /**
     * 修改角色
     *
     * @param id
     * @param roleDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update/{id}", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_UPDATE)
    @OperationLog(operationType = OperationType.UPDATE, title = "修改角色")
    public IView update(@PathVariable String id,
                        @VRequired(msg = "最后修改时间(乐观锁)不能为空")
                        @RequestParam Long lastModifyTime,
                        @VModel @ModelBind SecurityRoleDTO roleDTO) throws Exception {
        return iRoleService.update(id, lastModifyTime, roleDTO.toBean()).toMxJsonView();
    }

    /**
     * 角色详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_DETAIL)
    public IView detail(@PathVariable String id) throws Exception {
        return iRoleService.detail(id).toMxJsonView();
    }

    /**
     * 删除角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_DELETE)
    @OperationLog(operationType = OperationType.DELETE, title = "删除角色")
    public IView delete(@VRequired(msg = "ids不能为空")
                        @RequestParam("ids[]") String[] ids) throws Exception {
        return iRoleService.delete(ids).toMxJsonView();
    }


    /**
     * 角色权限列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/permission/list")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_PERMISSION_LIST)
    public IView permissionList(@VRequired(msg = "id不能为空")
                                @RequestParam String id) throws Exception {
        return iRoleService.permissionList(id).toMxJsonView();
    }


    /**
     * 角色权限绑定
     *
     * @param id
     * @param permissions
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/permission/bind", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_PERMISSION_BIND)
    @OperationLog(operationType = OperationType.OTHER, title = "角色权限绑定")
    public IView permissionBind(@VRequired(msg = "id不能为空")
                                @RequestParam String id,
                                @RequestParam(value = "permissions[]") String[] permissions) throws Exception {
        return iRoleService.permissionBind(id, permissions).toMxJsonView();
    }


}
