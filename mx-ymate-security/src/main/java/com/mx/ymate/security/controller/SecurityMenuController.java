package com.mx.ymate.security.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.dto.SecurityMenuDTO;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.service.ISecurityMenuService;
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
@RequestMapping("/menu")
public class SecurityMenuController {

    @Inject
    private ISecurityMenuService iMenuService;


    /**
     * 左侧导航栏
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/nav")
    @SaCheckLogin
    public IView nav() throws Exception {
        return iMenuService.nav().toMxJsonView();
    }


    /**
     * 菜单列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_LIST)
    public IView list() throws Exception {
        return iMenuService.list().toMxJsonView();
    }

    /**
     * 添加菜单
     *
     * @param menuDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_CREATE)
    @OperationLog(operationType = OperationType.CREATE, title = "添加菜单")
    public IView create(@VModel @ModelBind SecurityMenuDTO menuDTO) throws Exception {
        return iMenuService.create(menuDTO.toBean()).toMxJsonView();
    }

    /**
     * 修改菜单
     *
     * @param id
     * @param menuDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_UPDATE)
    @OperationLog(operationType = OperationType.UPDATE, title = "修改菜单")
    public IView update(@VRequired(msg = "ID不能为空")
                        @RequestParam String id,
                        @VModel @ModelBind SecurityMenuDTO menuDTO) throws Exception {
        return iMenuService.update(id, menuDTO.toBean()).toMxJsonView();
    }

    /**
     * 菜单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_DETAIL)
    public IView detail(@VRequired(msg = "ID不能为空")
                        @RequestParam String id) throws Exception {
        return iMenuService.detail(id).toMxJsonView();
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_DELETE)
    @OperationLog(operationType = OperationType.DELETE, title = "删除菜单")
    public IView delete(@VRequired(msg = "ID不能为空")
                        @RequestParam String id) throws Exception {
        return iMenuService.delete(id).toMxJsonView();
    }

    /**
     * 菜单角色列表
     *
     * @param menuId
     * @param name
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/role/list")
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_ROLE_LIST)
    public IView roleList(@VRequired(msg = "菜单ID不能为空")
                          @RequestParam String menuId,
                          @RequestParam String name,
                          @ModelBind PageDTO pageDTO) throws Exception {
        return iMenuService.roleList(menuId, name, pageDTO.toBean()).toMxJsonView();
    }

    /**
     * 添加菜单角色
     *
     * @param menuId
     * @param menuId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/role/create", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_ROLE_CREATE)
    @OperationLog(operationType = OperationType.CREATE, title = "添加菜单角色")
    public IView roleCreate(@VRequired(msg = "菜单ID不能为空")
                            @RequestParam String menuId,
                            @VRequired(msg = "角色ID不能为空")
                            @RequestParam String roleId) throws Exception {
        return iMenuService.roleCreate(menuId, roleId).toMxJsonView();
    }

    /**
     * 删除菜单角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/role/delete", method = Type.HttpMethod.POST)
    @SaCheckLogin
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_ROLE_DELETE)
    @OperationLog(operationType = OperationType.DELETE, title = "删除菜单角色")
    public IView roleDelete(@VRequired(msg = "ids不能为空")
                            @RequestParam("ids[]") String[] ids) throws Exception {
        return iMenuService.roleDelete(ids).toMxJsonView();
    }
}
