package com.mx.ymate.security.base.dto;

import com.mx.ymate.dev.support.mvc.i18n.validate.VMxRequired;
import com.mx.ymate.dev.util.BeanUtil;
import com.mx.ymate.security.base.bean.SecurityUserBean;
import net.ymate.platform.webmvc.annotation.RequestParam;

import java.io.Serializable;

import static com.mx.ymate.security.ValidateConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class SecurityUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @VMxRequired(msg = USER_NAME_NOT_EMPTY_MSG, i18nKey = USER_NAME_NOT_EMPTY_I18N_KEY)
    @RequestParam
    private String userName;

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

    public SecurityUserBean toBean() throws Exception {
        return BeanUtil.copy(this, SecurityUserBean::new);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return "SecurityUserDTO{" + "userName='" + userName + '\'' + ", realName='" + realName + '\'' + ", photoUri='" + photoUri + '\'' + ", mobile='" + mobile + '\'' + ", gender=" + gender + '}';
    }
}
