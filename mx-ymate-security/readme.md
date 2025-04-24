### 国际化说明

1. 菜单 权限  日志

### 调整sql
ALTER TABLE `mx-huanzhai`.`mx_security_menu`
ADD COLUMN `i18n_key` varchar(100) NULL COMMENT 'i18nkey' AFTER `name`;


ALTER TABLE `mx-huanzhai`.`mx_security_permission`
ADD COLUMN `group_i18n_key` varchar(100) NULL COMMENT '组名称i18nkey' AFTER `group_name`,
ADD COLUMN `permission_i18n_key` varchar(100) NULL COMMENT '权限名i18nkey' AFTER `permission_name`;