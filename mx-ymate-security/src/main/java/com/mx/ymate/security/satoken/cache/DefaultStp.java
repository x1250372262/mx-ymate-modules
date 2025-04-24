package com.mx.ymate.security.satoken.cache;

import cn.dev33.satoken.stp.StpInterface;
import com.mx.ymate.security.SaUtil;
import net.ymate.platform.core.beans.annotation.Bean;

import java.util.List;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@Bean
public class DefaultStp implements StpInterface {


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
