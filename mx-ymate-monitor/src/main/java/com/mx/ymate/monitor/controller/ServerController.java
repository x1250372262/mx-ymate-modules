package com.mx.ymate.monitor.controller;

import com.mx.ymate.dev.support.page.PageDTO;
import com.mx.ymate.monitor.service.IServerService;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.view.IView;

/**
 * @Author: xujianpeng.
 * @Create: 2024/2/23 11:38
 * @Description:
 */
@Controller
@RequestMapping("/server")
public class ServerController {

    @Inject
    private IServerService iServerService;

    /**
     * 添加
     *
     * @param name
     * @param ip
     * @param user
     * @param password
     * @param remark
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = Type.HttpMethod.POST)
    public IView create(@VRequired(msg = "名称不能为空")
                        @RequestParam String name,
                        @VRequired(msg = "ip不能为空")
                        @RequestParam String ip,
                        @VRequired(msg = "用户不能为空")
                        @RequestParam String user,
                        @VRequired(msg = "密码不能为空")
                        @RequestParam String password,
                        @RequestParam String remark) throws Exception {

        return iServerService.create(name, ip, user, password, remark).toJsonView();
    }

    /**
     * 修改
     *
     * @param id
     * @param name
     * @param ip
     * @param user
     * @param password
     * @param remark
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update/{id}", method = Type.HttpMethod.POST)
    public IView update(@PathVariable String id,
                        @VRequired(msg = "名称不能为空")
                        @RequestParam String name,
                        @VRequired(msg = "ip不能为空")
                        @RequestParam String ip,
                        @VRequired(msg = "用户不能为空")
                        @RequestParam String user,
                        @VRequired(msg = "密码不能为空")
                        @RequestParam String password,
                        @RequestParam String remark) throws Exception {

        return iServerService.update(id, name, ip, user, password, remark).toJsonView();
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = Type.HttpMethod.POST)
    public IView delete(@VRequired(msg = "ids不能为空")
                        @RequestParam(value = "ids[]") String[] ids) throws Exception {

        return iServerService.delete(ids).toJsonView();
    }

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    public IView detail(@PathVariable String id) throws Exception {
        return iServerService.detail(id).toJsonView();
    }

    /**
     * 列表
     *
     * @param name
     * @param ip
     * @param pageDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public IView list(@RequestParam String name,
                      @RequestParam String ip,
                      @ModelBind PageDTO pageDTO) throws Exception {
        return iServerService.list(name, ip, pageDTO.toBean()).toJsonView();
    }

}
