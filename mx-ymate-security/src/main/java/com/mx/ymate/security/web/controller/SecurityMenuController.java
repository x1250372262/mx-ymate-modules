package com.mx.ymate.security.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.dto.SecurityMenuDTO;
import com.mx.ymate.security.web.service.ISecurityMenuService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.annotation.VModel;
import net.ymate.platform.webmvc.annotation.Controller;
import net.ymate.platform.webmvc.annotation.ModelBind;
import net.ymate.platform.webmvc.annotation.RequestMapping;
import net.ymate.platform.webmvc.annotation.RequestParam;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

import static com.mx.ymate.security.I18nConstant.ID_NOT_EMPTY_I18N_KEY;
import static com.mx.ymate.security.I18nConstant.ID_NOT_EMPTY_MSG;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
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
    public IView nav() throws Exception {
        return iMenuService.nav().toJsonView();
    }


    /**
     * 菜单列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_LIST)
    public IView list() throws Exception {
        return iMenuService.list().toJsonView();
    }

    /**
     * 添加菜单
     *
     * @param menuDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_CREATE)
    public IView create(@VModel @ModelBind SecurityMenuDTO menuDTO) throws Exception {
        return iMenuService.create(menuDTO.toBean()).toJsonView();
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
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_UPDATE)
    public IView update(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                        @RequestParam String id,
                        @VModel @ModelBind SecurityMenuDTO menuDTO) throws Exception {
        return iMenuService.update(id, menuDTO.toBean()).toJsonView();
    }

    /**
     * 菜单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_DETAIL)
    public IView detail(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                        @RequestParam String id) throws Exception {
        return iMenuService.detail(id).toJsonView();
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_MENU_DELETE)
    public IView delete(@VMxRequired(msg = ID_NOT_EMPTY_MSG, i18nKey = ID_NOT_EMPTY_I18N_KEY)
                        @RequestParam String id) throws Exception {
        return iMenuService.delete(id).toJsonView();
    }

}
