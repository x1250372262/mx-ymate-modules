package com.mx.ymate.security.base.bean;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: 角色bean
 */
public class SecurityRoleBean implements Serializable {


    private String name;

    private String remark;


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
        return "SecurityRoleBean{" + "name='" + name + '\'' + ", remark='" + remark + '\'' + '}';
    }
}
