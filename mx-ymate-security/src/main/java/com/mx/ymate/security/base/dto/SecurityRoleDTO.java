package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityRoleBean;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

import static com.mx.ymate.security.I18nConstant.NAME_NOT_EMPTY_I18N_KEY;
import static com.mx.ymate.security.I18nConstant.NAME_NOT_EMPTY_MSG;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @VMxRequired(msg = NAME_NOT_EMPTY_MSG, i18nKey = NAME_NOT_EMPTY_I18N_KEY)
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
