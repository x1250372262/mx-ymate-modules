package com.mx.ymate.security.sql;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.security.base.config.SecurityPermissionConfig;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: mengxiang.
 * @create: 2021-10-27 09:00
 * @Description:
 */
public class SecuritySql {

    private static void menuSql(List<String> sqlList, String dbName, String client, String userId, long time, String prefix) {
        String sql = "INSERT INTO `{}`.`{}security_menu` (`id`, `client`, `type`, `parent_id`, `name`, `icon`, `path`, `url`, `sort`, `hide_status`, `permission`, `create_time`, `create_user`, `last_modify_time`, `last_modify_user`) VALUES ('{}', '{}',{}, '{}', '{}', '{}', '{}', '{}', {}, {},'{}',{},'{}',{},'{}');";
        String id = UUIDUtils.UUID();
        //首页
        sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, 1, "0", "首页", "mdi mdi-home", "", "/admin/home.html", 0, 0, "", time, userId, time, userId));
        //安全
        sqlList.add(StrUtil.format(sql, dbName, prefix, id, client, 2, "0", "安全管理", "mdi mdi-alarm-light", "/security", "", 100, 0, "SECURITY_MANAGER", time, userId, time, userId));
        sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, 0, id, "菜单管理", "", "", "/security/menu/list.html", 1, 0, "SECURITY_MENU_LIST", time, userId, time, userId));
        sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, 0, id, "人员管理", "", "", "/security/user/list.html", 3, 0, "SECURITY_USER_LIST", time, userId, time, userId));
        sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, 0, id, "角色管理", "", "", "/security/role/list.html", 2, 0, "SECURITY_ROLE_LIST", time, userId, time, userId));
        sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, 0, id, "日志管理", "", "", "/security/log/list.html", 4, 0, "SECURITY_LOG_LIST", time, userId, time, userId));
    }

    private static void userSql(List<String> sqlList, String dbName, String client, String userId, long time, String prefix) {

        String salt = RandomUtil.randomString(6);
        String password = DigestUtils.md5Hex("admin".getBytes(StandardCharsets.UTF_8));
        password = DigestUtils.md5Hex(Base64.encodeBase64((password + salt).getBytes(StandardCharsets.UTF_8)));
        String sql = "INSERT INTO `{}`.`{}security_user` (`id`, `resource_id`,`client`, `user_name`, `real_name`, `photo_uri`, `password`, `mobile`, `gender`, `create_user`, `create_time`, `last_modify_time`, `last_modify_user`, `salt`, `disable_status`, `founder`, `login_error_count`, `login_lock_status`, `login_lock_start_time`, `login_lock_end_time`) " +
                "VALUES ('{}', '{}', '{}', '{}', '{}', '{}', '{}','{}', {}, '{}', {}, {}, '{}', '{}', {}, {}, {}, {}, {}, {});";
        sqlList.add(StrUtil.format(sql, dbName, prefix, userId, client, client, "admin", "管理员", "", password, "13111111111", 1, userId, time, time, userId, salt, 0, 1, 0, 0, 0, 0));
    }

    private static void permissionSql(List<String> sqlList, String dbName, String client, String userId, long time, String prefix) {
        String sql = "INSERT INTO `{}`.`{}security_permission` (`id`, `resource_id`,`client`, `group_name`, `permission_name`, `permission_code`, `create_user`, `create_time`, `last_modify_user`, `last_modify_time`) VALUES ('{}', '{}', '{}', '{}', '{}', '{}','{}', {}, '{}', {});";
        SecurityPermissionConfig.permissionList().forEach(permission -> sqlList.add(StrUtil.format(sql, dbName, prefix, UUIDUtils.UUID(), client, client, permission.getGroupName(), permission.getName(), permission.getCode(), userId, time, userId, time)));
    }

    private static void outFile(File outFile, List<String> sqlList) {
        FileWriter.create(outFile, Charset.defaultCharset()).writeLines(sqlList);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入client:");
        String client = scanner.next();
        System.out.println("请输入dbName:");
        String dbName = scanner.next();
        System.out.println("请输入表前缀:");
        String prefix = scanner.next();
        if (StringUtils.isBlank(client) || StringUtils.isBlank(dbName) || StringUtils.isBlank(prefix)) {
            System.out.println("client和dbName和表前缀都不可以为空");
            return;
        }
        System.out.println(StrUtil.format("dbName:{},client:{},表前缀:{}", dbName, client, prefix));
        System.out.println("正在生成初始化数据。。。");
        long time = DateTimeUtils.currentTimeMillis();
        String userId = UUIDUtils.UUID();
        List<String> list = new ArrayList<>();
        menuSql(list, dbName, client, userId, time, prefix);
        userSql(list, dbName, client, userId, time, prefix);
        permissionSql(list, dbName, client, userId, time, prefix);
        outFile(new File(System.getProperty("user.dir"), "安全模块数据库" + File.separator + "security.sql"), list);
        System.out.println("生成成功");
    }


}
