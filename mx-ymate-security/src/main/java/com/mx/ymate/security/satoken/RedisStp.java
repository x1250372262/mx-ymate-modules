package com.mx.ymate.security.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.mx.ymate.security.SaUtil;
import net.ymate.platform.core.beans.annotation.Bean;

import java.util.List;


/**
 * @Author: mengxiang.
 * @create: 2021-09-04 15:43
 * @Description: 权限角色来源配置
 */
@Bean
public class RedisStp implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        try {
            return SaUtil.permissionList(loginId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
