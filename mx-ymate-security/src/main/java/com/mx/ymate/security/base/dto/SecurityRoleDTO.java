package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityRoleBean;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 角色信息
 */
public class SecurityRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @VRequired(msg = "名称不能为空")
    @RequestParam
    private String name;

    @RequestParam
    private String remark;

    public SecurityRoleBean toBean() throws Exception {
        return BeanUtil.copy(this, SecurityRoleBean::new);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SecurityRoleDTO{" + "name='" + name + '\'' + ", remark='" + remark + '\'' + '}';
    }
}
