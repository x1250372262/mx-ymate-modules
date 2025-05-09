package com.mx.ymate.security;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 安全模块验证常量
 */
public interface I18nConstant {

    /**
     * 验证注解常量
     */
    String USER_NAME_NOT_EMPTY_MSG = "用户名不能为空";
    String USER_NAME_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.USER_NAME_NOT_EMPTY";
    String REAL_NAME_NOT_EMPTY_MSG = "真实姓名不能为空";
    String REAL_NAME_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.REAL_NAME_NOT_EMPTY";
    String MOBILE_NOT_EMPTY_MSG = "手机号不能为空";
    String MOBILE_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.MOBILE_NOT_EMPTY";
    String GENDER_NOT_EMPTY_MSG = "性别不能为空";
    String GENDER_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.GENDER_NOT_EMPTY";
    String PARENT_ID_NOT_EMPTY_MSG = "父ID不能为空";
    String PARENT_ID_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.PARENT_ID_NOT_EMPTY";
    String NAME_NOT_EMPTY_MSG = "名称不能为空";
    String NAME_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.NAME_NOT_EMPTY";
    String SORT_NOT_EMPTY_MSG = "排序不能为空";
    String SORT_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.SORT_NOT_EMPTY";
    String TYPE_NOT_EMPTY_MSG = "类型不能为空";
    String TYPE_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.TYPE_NOT_EMPTY";
    String HIDE_NOT_EMPTY_MSG = "是否隐藏不能为空";
    String HIDE_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.HIDE_NOT_EMPTY";
    String PASSWORD_NOT_EMPTY_MSG = "密码不能为空";
    String PASSWORD_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.PASSWORD_NOT_EMPTY";
    String ID_NOT_EMPTY_MSG = "id不能为空";
    String ID_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.ID_NOT_EMPTY";
    String IDS_NOT_EMPTY_MSG = "ids不能为空";
    String IDS_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.IDS_NOT_EMPTY";
    String OLD_PASSWORD_NOT_EMPTY_MSG = "旧密码不能为空";
    String OLD_PASSWORD_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.OLD_PASSWORD_NOT_EMPTY";
    String NEW_PASSWORD_NOT_EMPTY_MSG = "新密码不能为空";
    String NEW_PASSWORD_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.NEW_PASSWORD_NOT_EMPTY";
    String RE_PASSWORD_NOT_EMPTY_MSG = "确认密码不能为空";
    String RE_PASSWORD_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.RE_PASSWORD_NOT_EMPTY";
    String LAST_MODIFY_TIME_NOT_EMPTY_MSG = "最后修改时间(乐观锁)不能为空";
    String LAST_MODIFY_TIME_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.LAST_MODIFY_TIME_NOT_EMPTY";
    String SCAN_QR_CODE_FLAG_NOT_EMPTY_MSG = "二维码登录标识不能为空";
    String SCAN_QR_CODE_FLAG_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.SCAN_QR_CODE_FLAG_NOT_EMPTY";
    String SCAN_LOGIN_ID_NOT_EMPTY_MSG = "登录id不能为空";
    String SCAN_LOGIN_ID_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.SCAN_LOGIN_ID_NOT_EMPTY";
    String SCAN_LOGIN_KEY_NOT_EMPTY_MSG = "loginKey不能为空";
    String SCAN_LOGIN_KEY_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.SCAN_LOGIN_KEY_NOT_EMPTY";
    String STATUS_NOT_EMPTY_MSG = "状态不能为空";
    String STATUS_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.STATUS_NOT_EMPTY";
    String USER_ID_NOT_EMPTY_MSG = "人员ID不能为空";
    String USER_ID_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.USER_ID_NOT_EMPTY";
    String ROLE_ID_NOT_EMPTY_MSG = "角色ID不能为空";
    String ROLE_ID_NOT_EMPTY_I18N_KEY = "MX.VALIDATE.SECURITY.ROLE_ID_NOT_EMPTY";


    /**
     * 权限组常量
     */
    String GROUP_SECURITY_MANAGER_MSG = "安全管理";
    String GROUP_SECURITY_MANAGER_I18N_KEY = "MX.GROUP.SECURITY_MANAGER";
    String GROUP_SECURITY_MENU_MSG = "菜单管理";
    String GROUP_SECURITY_MENU_I18N_KEY = "MX.GROUP.SECURITY_MENU";
    String GROUP_SECURITY_ROLE_MSG = "角色管理";
    String GROUP_SECURITY_ROLE_I18N_KEY = "MX.GROUP.SECURITY_ROLE";
    String GROUP_SECURITY_USER_MSG = "人员管理";
    String GROUP_SECURITY_USER_I18N_KEY = "MX.GROUP.SECURITY_USER";
    String GROUP_SECURITY_LOG_MSG = "日志管理";
    String GROUP_SECURITY_LOG_I18N_KEY = "MX.GROUP.SECURITY_LOG";

    /**
     * 权限常量
     */
    String PERMISSION_SECURITY_MANAGER_MSG = "安全管理";
    String PERMISSION_SECURITY_MANAGER_I18N_KEY = "MX.PERMISSION.SECURITY_MANAGER";
    String PERMISSION_SECURITY_MENU_LIST_MSG = "菜单列表";
    String PERMISSION_SECURITY_MENU_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_MENU_LIST";
    String PERMISSION_SECURITY_MENU_CREATE_MSG = "添加菜单";
    String PERMISSION_SECURITY_MENU_CREATE_I18N_KEY = "MX.PERMISSION.SECURITY_MENU_CREATE";
    String PERMISSION_SECURITY_MENU_UPDATE_MSG = "修改菜单";
    String PERMISSION_SECURITY_MENU_UPDATE_I18N_KEY = "MX.PERMISSION.SECURITY_MENU_UPDATE";
    String PERMISSION_SECURITY_MENU_DETAIL_MSG = "菜单详情";
    String PERMISSION_SECURITY_MENU_DETAIL_I18N_KEY = "MX.PERMISSION.SECURITY_MENU_DETAIL";
    String PERMISSION_SECURITY_MENU_DELETE_MSG = "删除菜单";
    String PERMISSION_SECURITY_MENU_DELETE_I18N_KEY = "MX.PERMISSION.SECURITY_MENU_DELETE";
    String PERMISSION_SECURITY_ROLE_CREATE_MSG = "添加角色";
    String PERMISSION_SECURITY_ROLE_CREATE_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_CREATE";
    String PERMISSION_SECURITY_ROLE_UPDATE_MSG = "修改角色";
    String PERMISSION_SECURITY_ROLE_UPDATE_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_UPDATE";
    String PERMISSION_SECURITY_ROLE_LIST_MSG = "角色列表";
    String PERMISSION_SECURITY_ROLE_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_LIST";
    String PERMISSION_SECURITY_ROLE_DETAIL_MSG = "角色详情";
    String PERMISSION_SECURITY_ROLE_DETAIL_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_DETAIL";
    String PERMISSION_SECURITY_ROLE_DELETE_MSG = "删除角色";
    String PERMISSION_SECURITY_ROLE_DELETE_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_DELETE";
    String PERMISSION_SECURITY_ROLE_PERMISSION_LIST_MSG = "角色权限列表";
    String PERMISSION_SECURITY_ROLE_PERMISSION_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_PERMISSION_LIST";
    String PERMISSION_SECURITY_ROLE_PERMISSION_BIND_MSG = "角色授权";
    String PERMISSION_SECURITY_ROLE_PERMISSION_BIND_I18N_KEY = "MX.PERMISSION.SECURITY_ROLE_PERMISSION_BIND";
    String PERMISSION_SECURITY_USER_LIST_MSG = "人员列表";
    String PERMISSION_SECURITY_USER_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_USER_LIST";
    String PERMISSION_SECURITY_USER_CREATE_MSG = "添加人员";
    String PERMISSION_SECURITY_USER_CREATE_I18N_KEY = "MX.PERMISSION.SECURITY_USER_CREATE";
    String PERMISSION_SECURITY_USER_UPDATE_STATUS_MSG = "修改人员状态";
    String PERMISSION_SECURITY_USER_UPDATE_STATUS_I18N_KEY = "MX.PERMISSION.SECURITY_USER_UPDATE_STATUS";
    String PERMISSION_SECURITY_USER_UNLOCK_MSG = "解除人员冻结";
    String PERMISSION_SECURITY_USER_UNLOCK_I18N_KEY = "MX.PERMISSION.SECURITY_USER_UNLOCK";
    String PERMISSION_SECURITY_USER_RESET_PASSWORD_MSG = "重置人员密码";
    String PERMISSION_SECURITY_USER_RESET_PASSWORD_I18N_KEY = "MX.PERMISSION.SECURITY_USER_RESET_PASSWORD";
    String PERMISSION_SECURITY_USER_DETAIL_MSG = "人员详情";
    String PERMISSION_SECURITY_USER_DETAIL_I18N_KEY = "MX.PERMISSION.SECURITY_USER_DETAIL";
    String PERMISSION_SECURITY_USER_ROLE_LIST_MSG = "人员角色列表";
    String PERMISSION_SECURITY_USER_ROLE_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_USER_ROLE_LIST";
    String PERMISSION_SECURITY_USER_ROLE_CREATE_MSG = "添加人员角色";
    String PERMISSION_SECURITY_USER_ROLE_CREATE_I18N_KEY = "MX.PERMISSION.SECURITY_USER_ROLE_CREATE";
    String PERMISSION_SECURITY_USER_ROLE_DELETE_MSG = "删除人员角色";
    String PERMISSION_SECURITY_USER_ROLE_DELETE_I18N_KEY = "MX.PERMISSION.SECURITY_USER_ROLE_DELETE";
    String PERMISSION_SECURITY_LOG_LIST_MSG = "日志列表";
    String PERMISSION_SECURITY_LOG_LIST_I18N_KEY = "MX.PERMISSION.SECURITY_LOG_LIST";
    String PERMISSION_SECURITY_LOG_DETAIL_MSG = "日志详情";
    String PERMISSION_SECURITY_LOG_DETAIL_I18N_KEY = "MX.PERMISSION.SECURITY_LOG_DETAIL";
    String PERMISSION_SECURITY_LOG_DELETE_MSG = "删除日志";
    String PERMISSION_SECURITY_LOG_DELETE_I18N_KEY = "MX.PERMISSION.SECURITY_LOG_DELETE";

    /**
     * 菜单常量
     */
    String MENU_INDEX_MSG = "首页";
    String MENU_INDEX_I18N_KEY = "MX.MENU.INDEX";
    String MENU_SECURITY_MANAGER_MSG = "安全管理";
    String MENU_SECURITY_MANAGER_I18N_KEY = "MX.MENU.SECURITY_MANAGER";
    String MENU_SECURITY_MENU_LIST_MSG = "菜单管理";
    String MENU_SECURITY_MENU_LIST_I18N_KEY = "MX.MENU.SECURITY_MENU_LIST";
    String MENU_SECURITY_USER_LIST_MSG = "人员管理";
    String MENU_SECURITY_USER_LIST_I18N_KEY = "MX.MENU.SECURITY_USER_LIST";
    String MENU_SECURITY_ROLE_LIST_MSG = "角色管理";
    String MENU_SECURITY_ROLE_LIST_I18N_KEY = "MX.MENU.SECURITY_ROLE_LIST";
    String MENU_SECURITY_LOG_LIST_MSG = "日志管理";
    String MENU_SECURITY_LOG_LIST_I18N_KEY = "MX.MENU.SECURITY_LOG_LIST";

    /**
     * 日志常量
     */
    String LOG_OPERATION_TYPE_UNKNOWN_MSG = "未知";
    String LOG_OPERATION_TYPE_UNKNOWN_I18N_KEY = "MX.LOG.OPERATION_TYPE_UNKNOWN";
    String LOG_OPERATION_TYPE_CREATE_MSG = "新增";
    String LOG_OPERATION_TYPE_CREATE_I18N_KEY = "MX.LOG.OPERATION_TYPE_CREATE";
    String LOG_OPERATION_TYPE_UPDATE_MSG = "修改";
    String LOG_OPERATION_TYPE_UPDATE_I18N_KEY = "MX.LOG.OPERATION_TYPE_UPDATE";
    String LOG_OPERATION_TYPE_DELETE_MSG = "删除";
    String LOG_OPERATION_TYPE_DELETE_I18N_KEY = "MX.LOG.OPERATION_TYPE_DELETE";
    String LOG_OPERATION_TYPE_OTHER_MSG = "其他";
    String LOG_OPERATION_TYPE_OTHER_I18N_KEY = "MX.LOG.OPERATION_TYPE_OTHER";
    String LOG_OPERATION_TYPE_LOGIN_MSG = "登录";
    String LOG_OPERATION_TYPE_LOGIN_I18N_KEY = "MX.LOG.OPERATION_TYPE_LOGIN";
    String LOG_LOGOUT_SUCCESS_MSG = "退出成功";
    String LOG_LOGOUT_SUCCESS_I18N_KEY = "MX.LOG.LOGOUT_SUCCESS";
    String LOG_LOGOUT_TITLE_MSG = "管理员退出";
    String LOG_LOGOUT_TITLE_I18N_KEY = "MX.LOG.LOGOUT_TITLE";
    String LOG_LOGIN_TITLE_MSG = "管理员登录";
    String LOG_LOGIN_TITLE_I18N_KEY = "MX.LOG.LOGIN_TITLE";
    String LOG_SCAN_LOGIN_TITLE_MSG = "管理员扫码登录";
    String LOG_SCAN_LOGIN_TITLE_I18N_KEY = "MX.LOG.SCAN_LOGIN_TITLE";
    String LOG_UNLOCK_TITLE_MSG = "解锁账号";
    String LOG_UNLOCK_TITLE_I18N_KEY = "MX.LOG.UNLOCK_TITLE";
    String LOG_LOCK_TITLE_MSG = "锁定账号";
    String LOG_LOCK_TITLE_I18N_KEY = "MX.LOG.LOCK_TITLE";
    String LOG_ADMIN_UPDATE_TITLE_MSG = "修改管理员信息";
    String LOG_ADMIN_UPDATE_TITLE_I18N_KEY = "MX.LOG.ADMIN_UPDATE_TITLE";
    String LOG_ADMIN_UPDATE_PASSWORD_TITLE_MSG = "修改管理员密码";
    String LOG_ADMIN_UPDATE_PASSWORD_TITLE_I18N_KEY = "MX.LOG.ADMIN_UPDATE_PASSWORD_TITLE";
    String LOG_MENU_CREATE_TITLE_MSG = "添加菜单";
    String LOG_MENU_CREATE_TITLE_I18N_KEY = "MX.LOG.MENU_CREATE_TITLE";
    String LOG_MENU_UPDATE_TITLE_MSG = "修改菜单";
    String LOG_MENU_UPDATE_TITLE_I18N_KEY = "MX.LOG.MENU_UPDATE_TITLE";
    String LOG_MENU_DELETE_TITLE_MSG = "删除菜单";
    String LOG_MENU_DELETE_TITLE_I18N_KEY = "MX.LOG.MENU_DELETE_TITLE";
    String LOG_OPERATION_LOG_DELETE_TITLE_MSG = "删除日志";
    String LOG_OPERATION_LOG_DELETE_TITLE_I18N_KEY = "MX.LOG.OPERATION_LOG_DELETE_TITLE";
    String LOG_ROLE_CREATE_TITLE_MSG = "添加角色";
    String LOG_ROLE_CREATE_TITLE_I18N_KEY = "MX.LOG.ROLE_CREATE_TITLE";
    String LOG_ROLE_UPDATE_TITLE_MSG = "修改角色";
    String LOG_ROLE_UPDATE_TITLE_I18N_KEY = "MX.LOG.ROLE_UPDATE_TITLE";
    String LOG_ROLE_DELETE_TITLE_MSG = "删除角色";
    String LOG_ROLE_DELETE_TITLE_I18N_KEY = "MX.LOG.ROLE_DELETE_TITLE";
    String LOG_ROLE_PERMISSION_BIND_TITLE_MSG = "角色权限绑定";
    String LOG_ROLE_PERMISSION_BIND_TITLE_I18N_KEY = "MX.LOG.ROLE_PERMISSION_BIND_TITLE";
    String LOG_USER_CREATE_TITLE_MSG = "添加人员";
    String LOG_USER_CREATE_TITLE_I18N_KEY = "MX.LOG.USER_CREATE_TITLE";
    String LOG_USER_UPDATE_STATUS_TITLE_MSG = "修改状态";
    String LOG_USER_UPDATE_STATUS_TITLE_I18N_KEY = "MX.LOG.USER_UPDATE_STATUS_TITLE";
    String LOG_USER_UNLOCK_TITLE_MSG = "人员解锁";
    String LOG_USER_UNLOCK_TITLE_I18N_KEY = "MX.LOG.USER_UNLOCK_TITLE";
    String LOG_USER_RESET_PASSWORD_TITLE_MSG = "重置密码";
    String LOG_USER_RESET_PASSWORD_TITLE_I18N_KEY = "MX.LOG.USER_RESET_PASSWORD_TITLE";
    String LOG_USER_ROLE_CREATE_TITLE_MSG = "添加人员角色";
    String LOG_USER_ROLE_CREATE_TITLE_I18N_KEY = "MX.LOG.USER_ROLE_CREATE_TITLE";
    String LOG_USER_ROLE_DELETE_TITLE_MSG = "删除人员角色";
    String LOG_USER_ROLE_DELETE_TITLE_I18N_KEY = "MX.LOG.USER_ROLE_DELETE_TITLE";
}
