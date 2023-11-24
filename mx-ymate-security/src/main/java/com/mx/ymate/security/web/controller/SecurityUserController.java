package com.mx.ymate.security.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import com.mx.ymate.security.base.dto.SecurityUserDTO;
import com.mx.ymate.security.web.service.ISecurityUserService;
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
@RequestMapping("/user")
public class SecurityUserController {

    @Inject
    private ISecurityUserService iUserService;


    /**
     * 人员列表
     *
     * @param userName
     * @param realName
     * @param disableStatus
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_LIST)
    public IView list(@RequestParam String userName,
                      @RequestParam String realName,
                      @RequestParam Integer disableStatus,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iUserService.list(userName, realName, disableStatus, pageDTO.toBean()).toJsonView();
    }


    /**
     * 人员下拉选
     *
     * @param disableStatus
     * @return
     * @throws Exception
     */
    @RequestMapping("/select")
    public IView select(@RequestParam Integer disableStatus) throws Exception {
        return iUserService.select(disableStatus).toJsonView();
    }

    /**
     * 添加人员
     *
     * @param password
     * @param securityUserDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_CREATE)
    public IView create(@VRequired(msg = "密码不能为空")
                        @RequestParam String password,
                        @VModel @ModelBind SecurityUserDTO securityUserDTO) throws Exception {
        return iUserService.create(password, securityUserDTO.toBean()).toJsonView();
    }

    /**
     * 修改状态
     *
     * @param id
     * @param lastModifyTime
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/status/{id}", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_UPDATE_STATUS)
    public IView status(@PathVariable String id,
                        @VRequired(msg = "最后修改时间(乐观锁)不能为空")
                        @RequestParam Long lastModifyTime,
                        @VRequired(msg = "状态不能为空")
                        @RequestParam Integer status) throws Exception {
        return iUserService.status(id, lastModifyTime, status).toJsonView();
    }

    /**
     * 解锁
     *
     * @param id
     * @param lastModifyTime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unlock/{id}", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_UNLOCK)
    public IView unlock(@PathVariable String id,
                        @VRequired(msg = "最后修改时间(乐观锁)不能为空")
                        @RequestParam Long lastModifyTime) throws Exception {
        return iUserService.unlock(id, lastModifyTime).toJsonView();
    }

    /**
     * 重置密码
     *
     * @param id
     * @param lastModifyTime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/resetPassword/{id}", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_RESET_PASSWORD)
    public IView resetPassword(@PathVariable String id,
                               @VRequired(msg = "最后修改时间(乐观锁)不能为空")
                               @RequestParam Long lastModifyTime) throws Exception {
        return iUserService.resetPassword(id, lastModifyTime).toJsonView();
    }

    /**
     * 人员详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_DETAIL)
    public IView detail(@PathVariable String id) throws Exception {
        return iUserService.detail(id).toJsonView();
    }


    /**
     * 人员角色列表
     *
     * @param userId
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/role/list")
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_ROLE_LIST)
    public IView roleList(@VRequired(msg = "人员ID不能为空")
                          @RequestParam String userId,
                          @ModelBind PageDTO pageDTO) throws Exception {
        return iUserService.roleList(userId, pageDTO.toBean()).toJsonView();
    }

    /**
     * 添加人员角色
     *
     * @param userId
     * @param roleId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/role/create", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_ROLE_CREATE)
    public IView roleCreate(@VRequired(msg = "人员ID不能为空")
                            @RequestParam String userId,
                            @VRequired(msg = "角色ID不能为空")
                            @RequestParam String roleId) throws Exception {
        return iUserService.roleCreate(userId, roleId).toJsonView();
    }

    /**
     * 删除人员角色
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/role/delete", method = Type.HttpMethod.POST)
    @SaCheckPermission(value = SecurityPermissionConfig.SECURITY_USER_ROLE_DELETE)
    public IView roleDelete(@VRequired(msg = "ids不能为空")
                            @RequestParam("ids[]") String[] ids) throws Exception {
        return iUserService.roleDelete(ids).toJsonView();
    }
}
