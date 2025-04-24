package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityLoginInfoBean;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

import static com.mx.ymate.security.I18nConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityLoginInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 真实姓名
     */
    @VMxRequired(msg = REAL_NAME_NOT_EMPTY_MSG, i18nKey = REAL_NAME_NOT_EMPTY_I18N_KEY)
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
    @VMxRequired(msg = MOBILE_NOT_EMPTY_MSG, i18nKey = MOBILE_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private String mobile;

    /**
     * 性别
     */
    @VMxRequired(msg = GENDER_NOT_EMPTY_MSG, i18nKey = GENDER_NOT_EMPTY_I18N_KEY)
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
