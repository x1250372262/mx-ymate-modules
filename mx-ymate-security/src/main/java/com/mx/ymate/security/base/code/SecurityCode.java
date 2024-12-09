package com.mx.ymate.security.base.code;

import com.mx.ymate.dev.code.ICode;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description:
 */
public enum SecurityCode implements ICode {

    /**
     * 安全错误码 70开头
     */
    SECURITY_CHECK_ERROR("MS000", "检查错误"),
    SECURITY_LOGIN_USER_NAME_NOT_EXIST("MS001", "用户名不存在"),
    SECURITY_LOGIN_USER_NAME_OR_PASSWORD_ERROR("MS002", "用户名或密码错误"),
    SECURITY_LOGIN_USER_LOCKED("MS003", "用户被锁住，请在{}后重试"),
    SECURITY_LOGIN_USER_DISABLE("MS004", "用户被禁用,请联系管理员!"),
    SECURITY_USER_PASSWORD_NOT_SAME("MS005", "两次密码输入不一致"),
    SECURITY_USER_OLD_PASSWORD_ERROR("MS006", "原密码错误"),
    SECURITY_USER_NEW_PASSWORD_NOT_SAME_OLD_PASSWORD("MS007", "新密码不能和原密码相同"),
    SECURITY_USER_ROLE_EXISTS("MS008", "该角色已经添加过了"),
    SECURITY_MENU_HAS_CHILD_NOT_DELETE("MS009", "有子菜单不能删除!"),
    SECURITY_MENU_ROLE_EXISTS("MS010", "该角色已经添加过了!"),
    SECURITY_LOGIN_USER_LOCK_SCREEN("MS011", "屏幕被锁定，请先解锁"),
    SECURITY_LOGIN_SCAN_QRCODE_EXPIRES_ERROR("MS012", "请设置正确的二维码过期时间"),
    SECURITY_LOGIN_SCAN_QRCODE_CREATE_ERROR("MS013", "二维码申请失败"),
    SECURITY_LOGIN_SCAN_QRCODE_TIMEOUT("MS014", "二维码已失效"),
    SECURITY_LOGIN_SCAN_QRCODE_REPEAT("MS015", "该二维码已经被扫描过，请勿重复扫码"),
    SECURITY_LOGIN_SCAN_QRCODE_NEED_SCAN("MS016", "请先扫码"),
    SECURITY_LOGIN_USER_NOT_EXIST("MS017", "用户不存在");


    private final String code;
    private final String msg;

    SecurityCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
