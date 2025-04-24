package com.mx.ymate.security.base.config;

import com.mx.ymate.security.base.bean.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mx.ymate.security.I18nConstant.*;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 权限配置 常量 方法
 */
public class SecurityPermissionConfig {

    public static final String SECURITY_MENU_LIST = "SECURITY_MENU_LIST";
    public static final String SECURITY_MENU_CREATE = "SECURITY_MENU_CREATE";
    public static final String SECURITY_MENU_UPDATE = "SECURITY_MENU_UPDATE";
    public static final String SECURITY_MENU_DETAIL = "SECURITY_MENU_DETAIL";
    public static final String SECURITY_MENU_DELETE = "SECURITY_MENU_DELETE";

    public static final String SECURITY_ROLE_CREATE = "SECURITY_ROLE_CREATE";
    public static final String SECURITY_ROLE_UPDATE = "SECURITY_ROLE_UPDATE";
    public static final String SECURITY_ROLE_LIST = "SECURITY_ROLE_LIST";
    public static final String SECURITY_ROLE_DETAIL = "SECURITY_ROLE_DETAIL";
    public static final String SECURITY_ROLE_DELETE = "SECURITY_ROLE_DELETE";
    public static final String SECURITY_ROLE_PERMISSION_LIST = "SECURITY_ROLE_PERMISSION_LIST";
    public static final String SECURITY_ROLE_PERMISSION_BIND = "SECURITY_ROLE_PERMISSION_BIND";

    public static final String SECURITY_USER_LIST = "SECURITY_USER_LIST";
    public static final String SECURITY_USER_CREATE = "SECURITY_USER_CREATE";
    public static final String SECURITY_USER_UPDATE_STATUS = "SECURITY_USER_UPDATE_STATUS";
    public static final String SECURITY_USER_UNLOCK = "SECURITY_USER_UNLOCK";
    public static final String SECURITY_USER_RESET_PASSWORD = "SECURITY_USER_RESET_PASSWORD";
    public static final String SECURITY_USER_DETAIL = "SECURITY_USER_DETAIL";
    public static final String SECURITY_USER_ROLE_LIST = "SECURITY_USER_ROLE_LIST";
    public static final String SECURITY_USER_ROLE_CREATE = "SECURITY_USER_ROLE_CREATE";
    public static final String SECURITY_USER_ROLE_DELETE = "SECURITY_USER_ROLE_DELETE";

    public static final String SECURITY_LOG_LIST = "SECURITY_LOG_LIST";

    public static final String SECURITY_LOG_DETAIL = "SECURITY_LOG_DETAIL";
    public static final String SECURITY_LOG_DELETE = "SECURITY_LOG_DELETE";
    private static final List<Permission> PERMISSION_LIST = new ArrayList<>();

    static {
        //安全管理
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MANAGER.value(), GroupEnum.SECURITY_MANAGER.i18nKey(), PermissionEnum.SECURITY_MANAGER.value(), PermissionEnum.SECURITY_MANAGER.i18nKey(), PermissionEnum.SECURITY_MANAGER.name()));
        //菜单管理
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MENU.value(), GroupEnum.SECURITY_MENU.i18nKey(), PermissionEnum.SECURITY_MENU_LIST.value(), PermissionEnum.SECURITY_MENU_LIST.i18nKey(), PermissionEnum.SECURITY_MENU_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MENU.value(), GroupEnum.SECURITY_MENU.i18nKey(), PermissionEnum.SECURITY_MENU_CREATE.value(), PermissionEnum.SECURITY_MENU_CREATE.i18nKey(), PermissionEnum.SECURITY_MENU_CREATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MENU.value(), GroupEnum.SECURITY_MENU.i18nKey(), PermissionEnum.SECURITY_MENU_UPDATE.value(), PermissionEnum.SECURITY_MENU_UPDATE.i18nKey(), PermissionEnum.SECURITY_MENU_UPDATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MENU.value(), GroupEnum.SECURITY_MENU.i18nKey(), PermissionEnum.SECURITY_MENU_DETAIL.value(), PermissionEnum.SECURITY_MENU_DETAIL.i18nKey(), PermissionEnum.SECURITY_MENU_DETAIL.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_MENU.value(), GroupEnum.SECURITY_MENU.i18nKey(), PermissionEnum.SECURITY_MENU_DELETE.value(), PermissionEnum.SECURITY_MENU_DELETE.i18nKey(), PermissionEnum.SECURITY_MENU_DELETE.name()));
        //角色管理
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_CREATE.value(), PermissionEnum.SECURITY_ROLE_CREATE.i18nKey(), PermissionEnum.SECURITY_ROLE_CREATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_UPDATE.value(), PermissionEnum.SECURITY_ROLE_UPDATE.i18nKey(), PermissionEnum.SECURITY_ROLE_UPDATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_LIST.value(), PermissionEnum.SECURITY_ROLE_LIST.i18nKey(), PermissionEnum.SECURITY_ROLE_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_DETAIL.value(), PermissionEnum.SECURITY_ROLE_DETAIL.i18nKey(), PermissionEnum.SECURITY_ROLE_DETAIL.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_DELETE.value(), PermissionEnum.SECURITY_ROLE_DELETE.i18nKey(), PermissionEnum.SECURITY_ROLE_DELETE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_PERMISSION_LIST.value(), PermissionEnum.SECURITY_ROLE_PERMISSION_LIST.i18nKey(), PermissionEnum.SECURITY_ROLE_PERMISSION_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_ROLE.value(), GroupEnum.SECURITY_ROLE.i18nKey(), PermissionEnum.SECURITY_ROLE_PERMISSION_BIND.value(), PermissionEnum.SECURITY_ROLE_PERMISSION_BIND.i18nKey(), PermissionEnum.SECURITY_ROLE_PERMISSION_BIND.name()));
        //人员管理
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_LIST.value(), PermissionEnum.SECURITY_USER_LIST.i18nKey(), PermissionEnum.SECURITY_USER_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_CREATE.value(), PermissionEnum.SECURITY_USER_CREATE.i18nKey(), PermissionEnum.SECURITY_USER_CREATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_UPDATE_STATUS.value(), PermissionEnum.SECURITY_USER_UPDATE_STATUS.i18nKey(), PermissionEnum.SECURITY_USER_UPDATE_STATUS.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_UNLOCK.value(), PermissionEnum.SECURITY_USER_UNLOCK.i18nKey(), PermissionEnum.SECURITY_USER_UNLOCK.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_RESET_PASSWORD.value(), PermissionEnum.SECURITY_USER_RESET_PASSWORD.i18nKey(), PermissionEnum.SECURITY_USER_RESET_PASSWORD.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_DETAIL.value(), PermissionEnum.SECURITY_USER_DETAIL.i18nKey(), PermissionEnum.SECURITY_USER_DETAIL.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_LIST.value(), PermissionEnum.SECURITY_USER_ROLE_LIST.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_CREATE.value(), PermissionEnum.SECURITY_USER_ROLE_CREATE.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_CREATE.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_USER.value(), GroupEnum.SECURITY_USER.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_DELETE.value(), PermissionEnum.SECURITY_USER_ROLE_DELETE.i18nKey(), PermissionEnum.SECURITY_USER_ROLE_DELETE.name()));
        //日志管理
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_LOG.value(), GroupEnum.SECURITY_LOG.i18nKey(), PermissionEnum.SECURITY_LOG_LIST.value(), PermissionEnum.SECURITY_LOG_LIST.i18nKey(), PermissionEnum.SECURITY_LOG_LIST.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_LOG.value(), GroupEnum.SECURITY_LOG.i18nKey(), PermissionEnum.SECURITY_LOG_DETAIL.value(), PermissionEnum.SECURITY_LOG_DETAIL.i18nKey(), PermissionEnum.SECURITY_LOG_DETAIL.name()));
        PERMISSION_LIST.add(new Permission(GroupEnum.SECURITY_LOG.value(), GroupEnum.SECURITY_LOG.i18nKey(), PermissionEnum.SECURITY_LOG_DELETE.value(), PermissionEnum.SECURITY_LOG_DELETE.i18nKey(), PermissionEnum.SECURITY_LOG_DELETE.name()));
    }

    public static List<Permission> permissionList() {
        return PERMISSION_LIST;
    }

    public static List<Permission> permissionList(String groupName) {
        return PERMISSION_LIST.stream().filter(permission -> permission.getGroupName().equals(groupName)).collect(Collectors.toList());
    }

    public enum GroupEnum {

        /**
         * 安全模块权限分组
         */
        SECURITY_MANAGER(GROUP_SECURITY_MANAGER_MSG, GROUP_SECURITY_MANAGER_I18N_KEY),
        SECURITY_MENU(GROUP_SECURITY_MENU_MSG, GROUP_SECURITY_MENU_I18N_KEY),
        SECURITY_ROLE(GROUP_SECURITY_ROLE_MSG, GROUP_SECURITY_ROLE_I18N_KEY),
        SECURITY_USER(GROUP_SECURITY_USER_MSG, GROUP_SECURITY_USER_I18N_KEY),
        SECURITY_LOG(GROUP_SECURITY_LOG_MSG, GROUP_SECURITY_LOG_I18N_KEY);

        private final String value;
        private final String i18nKey;

        GroupEnum(String value, String i18nKey) {
            this.value = value;
            this.i18nKey = i18nKey;
        }

        public String value() {
            return this.value;
        }

        public String i18nKey() {
            return this.i18nKey;
        }
    }

    public enum PermissionEnum {

        //安全管理
        SECURITY_MANAGER(PERMISSION_SECURITY_MANAGER_MSG, PERMISSION_SECURITY_MANAGER_I18N_KEY),
        //菜单管理
        SECURITY_MENU_LIST(PERMISSION_SECURITY_MENU_LIST_MSG, PERMISSION_SECURITY_MENU_LIST_I18N_KEY),
        SECURITY_MENU_CREATE(PERMISSION_SECURITY_MENU_CREATE_MSG, PERMISSION_SECURITY_MENU_CREATE_I18N_KEY),
        SECURITY_MENU_UPDATE(PERMISSION_SECURITY_MENU_UPDATE_MSG, PERMISSION_SECURITY_MENU_UPDATE_I18N_KEY),
        SECURITY_MENU_DETAIL(PERMISSION_SECURITY_MENU_DETAIL_MSG, PERMISSION_SECURITY_MENU_DETAIL_I18N_KEY),
        SECURITY_MENU_DELETE(PERMISSION_SECURITY_MENU_DELETE_MSG, PERMISSION_SECURITY_MENU_DELETE_I18N_KEY),
        //角色管理
        SECURITY_ROLE_CREATE(PERMISSION_SECURITY_ROLE_CREATE_MSG, PERMISSION_SECURITY_ROLE_CREATE_I18N_KEY),
        SECURITY_ROLE_UPDATE(PERMISSION_SECURITY_ROLE_UPDATE_MSG, PERMISSION_SECURITY_ROLE_UPDATE_I18N_KEY),
        SECURITY_ROLE_LIST(PERMISSION_SECURITY_ROLE_LIST_MSG, PERMISSION_SECURITY_ROLE_LIST_I18N_KEY),
        SECURITY_ROLE_DETAIL(PERMISSION_SECURITY_ROLE_DETAIL_MSG, PERMISSION_SECURITY_ROLE_DETAIL_I18N_KEY),
        SECURITY_ROLE_DELETE(PERMISSION_SECURITY_ROLE_DELETE_MSG, PERMISSION_SECURITY_ROLE_DELETE_I18N_KEY),
        SECURITY_ROLE_PERMISSION_LIST(PERMISSION_SECURITY_ROLE_PERMISSION_LIST_MSG, PERMISSION_SECURITY_ROLE_PERMISSION_LIST_I18N_KEY),
        SECURITY_ROLE_PERMISSION_BIND(PERMISSION_SECURITY_ROLE_PERMISSION_BIND_MSG, PERMISSION_SECURITY_ROLE_PERMISSION_BIND_I18N_KEY),
        //人员管理
        SECURITY_USER_LIST(PERMISSION_SECURITY_USER_LIST_MSG, PERMISSION_SECURITY_USER_LIST_I18N_KEY),
        SECURITY_USER_CREATE(PERMISSION_SECURITY_USER_CREATE_MSG, PERMISSION_SECURITY_USER_CREATE_I18N_KEY),
        SECURITY_USER_UPDATE_STATUS(PERMISSION_SECURITY_USER_UPDATE_STATUS_MSG, PERMISSION_SECURITY_USER_UPDATE_STATUS_I18N_KEY),
        SECURITY_USER_UNLOCK(PERMISSION_SECURITY_USER_UNLOCK_MSG, PERMISSION_SECURITY_USER_UNLOCK_I18N_KEY),
        SECURITY_USER_RESET_PASSWORD(PERMISSION_SECURITY_USER_RESET_PASSWORD_MSG, PERMISSION_SECURITY_USER_RESET_PASSWORD_I18N_KEY),
        SECURITY_USER_DETAIL(PERMISSION_SECURITY_USER_DETAIL_MSG, PERMISSION_SECURITY_USER_DETAIL_I18N_KEY),
        SECURITY_USER_ROLE_LIST(PERMISSION_SECURITY_USER_ROLE_LIST_MSG, PERMISSION_SECURITY_USER_ROLE_LIST_I18N_KEY),
        SECURITY_USER_ROLE_CREATE(PERMISSION_SECURITY_USER_ROLE_CREATE_MSG, PERMISSION_SECURITY_USER_ROLE_CREATE_I18N_KEY),
        SECURITY_USER_ROLE_DELETE(PERMISSION_SECURITY_USER_ROLE_DELETE_MSG, PERMISSION_SECURITY_USER_ROLE_DELETE_I18N_KEY),
        //日志管理
        SECURITY_LOG_LIST(PERMISSION_SECURITY_LOG_LIST_MSG, PERMISSION_SECURITY_LOG_LIST_I18N_KEY),
        SECURITY_LOG_DETAIL(PERMISSION_SECURITY_LOG_DETAIL_MSG, PERMISSION_SECURITY_LOG_DETAIL_I18N_KEY),
        SECURITY_LOG_DELETE(PERMISSION_SECURITY_LOG_DELETE_MSG, PERMISSION_SECURITY_LOG_DELETE_I18N_KEY);

        private final String value;
        private final String i18nKey;

        PermissionEnum(String value, String i18nKey) {
            this.value = value;
            this.i18nKey = i18nKey;
        }

        public String value() {
            return this.value;
        }

        public String i18nKey() {
            return this.i18nKey;
        }
    }


}
