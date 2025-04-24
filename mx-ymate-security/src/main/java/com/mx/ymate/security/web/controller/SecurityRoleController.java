package com.mx.ymate.security.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.dto.SecurityRoleDTO;
import com.mx.ymate.security.web.service.ISecurityRoleService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.annotation.VModel;
import net.ymate.platform.webmvc.annotation.*;
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
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_LIST)
    public IView list(@RequestParam String name,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iRoleService.list(name, pageDTO.toBean()).toJsonView();
    }

    /**
     * 添加角色
     *
     * @param roleDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_CREATE)
    public IView create(@VModel @ModelBind SecurityRoleDTO roleDTO) throws Exception {
        return iRoleService.create(roleDTO.toBean()).toJsonView();
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
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_UPDATE)
    public IView update(@PathVariable String id,
                        @VMxRequired(msg = LAST_MODIFY_TIME_NOT_EMPTY_MSG, i18nKey = LAST_MODIFY_TIME_NOT_EMPTY_I18N_KEY)
                        @RequestParam Long lastModifyTime,
                        @VModel @ModelBind SecurityRoleDTO roleDTO) throws Exception {
        return iRoleService.update(id, lastModifyTime, roleDTO.toBean()).toJsonView();
    }

    /**
     * 角色详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_DETAIL)
    public IView detail(@PathVariable String id) throws Exception {
        return iRoleService.detail(id).toJsonView();
    }

    /**
     * 删除角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_DELETE)
    public IView delete(@VMxRequired(msg = IDS_NOT_EMPTY_MSG, i18nKey = IDS_NOT_EMPTY_I18N_KEY)
                        @RequestParam("ids[]") String[] ids) throws Exception {
        return iRoleService.delete(ids).toJsonView();
    }


    /**
     * 角色权限列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/permission/list")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_PERMISSION_LIST)
    public IView permissionList(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                                @RequestParam String id) throws Exception {
        return iRoleService.permissionList(id).toJsonView();
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
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_ROLE_PERMISSION_BIND)
    public IView permissionBind(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                                @RequestParam String id,
                                @RequestParam(value = "permissions[]") String[] permissions) throws Exception {
        return iRoleService.permissionBind(id, permissions).toJsonView();
    }


}
