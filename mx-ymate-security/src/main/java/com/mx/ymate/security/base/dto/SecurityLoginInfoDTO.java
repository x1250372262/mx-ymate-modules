package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityLoginInfoBean;
import net.ymate.platform.validation.validate.VRequired;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

/**
 * @Author: mengxiang.
 * @create: 2021-09-03 15:08
 * @Description:
 */
public class SecurityLoginInfoDTO implements Serializable {

    /**
     * 真实姓名
     */
    @VRequired(msg = "不能为空")
    @RequestParam
    private String realName;

    /**
     * 头像
     */
    @RequestParam
    private String photoUri;

    /**
     * 手机号
     */
    @VRequired(msg = "不能为空")
    @RequestParam
    private String mobile;

    /**
     * 性别
     */
    @VRequired(msg = "不能为空")
    @RequestParam
    private Integer gender;

    public SecurityLoginInfoBean toBean() throws Exception {
        return BeanUtil.copy(this, SecurityLoginInfoBean::new);
    }


    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "SecurityLoginInfoDTO{" + "realName='" + realName + '\'' + ", photoUri='" + photoUri + '\'' + ", mobile='" + mobile + '\'' + ", gender=" + gender + '}';
    }
}
