package com.mx.ymate.security.base.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.dao.ISecurityUserDao;
import com.mx.ymate.security.service.ISecurityUserRoleService;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.mx.ymate.security.SaUtil.PERMISSION_LIST;


/**
 * @Author: mengxiang.
 * @create: 2021-09-04 15:43
 * @Description: 权限角色来源配置
 */
@Bean
public class SecurityStpImpl implements StpInterface {

    @Inject
    private ISecurityUserRoleService iSecurityUserRoleService;

    @Inject
    private ISecurityUserDao iSecurityUserDao;


    private final ISecurityConfig mxSecurityConfig = Security.get().getConfig();

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = new ArrayList<>();
        try {
            String permissionKey = StrUtil.format(PERMISSION_LIST, mxSecurityConfig.client(), SaUtil.getToken(), StpUtil.getLoginType(), SaUtil.loginId());
            String permissionStr = Convert.toStr(RedisApi.strGet(permissionKey));
            if (StringUtils.isNotBlank(permissionStr) && !SaUtil.isFounder()) {
                JSONArray redisPermissionList = JSONObject.parseArray(permissionStr);
                for (Object redisRole : redisPermissionList) {
                    permissionList.add(Convert.toStr(redisRole));
                }
            } else {
                permissionList = iSecurityUserRoleService.securityUserPermissionList((String) loginId, null);
                RedisApi.strSet(permissionKey, JSONObject.toJSONString(permissionList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }
}
