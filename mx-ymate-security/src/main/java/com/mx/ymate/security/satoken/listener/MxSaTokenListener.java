package com.mx.ymate.security.satoken.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.controller.SecurityLoginController;
import com.mx.ymate.security.dao.ISecurityUserDao;
import com.mx.ymate.security.event.OperationLogEvent;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: mengxiang.
 * @create: 2021-10-22 16:52
 * @Description:
 */
@Bean
public class MxSaTokenListener implements SaTokenListener {

    private final ISecurityConfig config = Security.get().getConfig();

    @Inject
    private ISecurityUserDao iSecurityUserDao;

    private SecurityOperationLog createOperationLog(String title, JSONObject jsonObject, String methodName, String loginId) throws Exception {
        SecurityUser securityUser = iSecurityUserDao.findById(loginId);
        HttpServletRequest request = WebContext.getRequest();
        String userAgentStr = request.getHeader("user-agent");
        // *========数据库日志=========*//
        return SecurityOperationLog.builder()
                .id(UUIDUtils.UUID())
                .resourceId(securityUser.getResourceId())
                .title(title)
                .client(config.client())
                .type(OperationType.LOGIN.name())
                .typeName(OperationType.LOGIN.value())
                .userId(loginId)
                .userName(StringUtils.defaultIfBlank(securityUser.getRealName(), securityUser.getUserName()))
                .createTime(DateTimeUtils.currentTimeMillis())
                .requestUrl(request.getRequestURI())
                .requestParam(JSONObject.toJSONString(ServletUtil.getParams(request)))
                .returnCode(jsonObject.getString("code"))
                .returnMessage(jsonObject.getString("msg"))
                .returnResult(jsonObject.toJSONString())
                .className(SecurityLoginController.class.getName())
                .methodName(methodName)
                .ip(ServletUtil.getClientIP(request))
                //位置之后再说把
                .location("")
                .os(UserAgentUtil.parse(userAgentStr).getOs().toString())
                .browser(UserAgentUtil.parse(userAgentStr).getBrowser().toString())
                .build();
    }


    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        if (config.openLog()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "00000");
            jsonObject.put("msg", "登录成功");
            try {
                SecurityOperationLog securityOperationLog = createOperationLog("管理员登录", jsonObject, "login", Convert.toStr(loginId));
                // 保存数据库
                // 创建事件对象
                OperationLogEvent operationLogEvent = new OperationLogEvent(YMP.get(), OperationLogEvent.EVENT.CREATE_LOG);
                // 为当前事件设置扩展参数
                operationLogEvent.addParamExtend("log", securityOperationLog);
                // 触发事件
                YMP.get().getEvents().fireEvent(operationLogEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        if (config.openLog()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "00000");
            jsonObject.put("msg", "退出成功");
            try {
                SecurityOperationLog securityOperationLog = createOperationLog("管理员退出", jsonObject, "logout", Convert.toStr(loginId));
                // 保存数据库
                // 创建事件对象
                OperationLogEvent operationLogEvent = new OperationLogEvent(YMP.get(), OperationLogEvent.EVENT.CREATE_LOG);
                // 为当前事件设置扩展参数
                operationLogEvent.addParamExtend("log", securityOperationLog);
                // 触发事件
                YMP.get().getEvents().fireEvent(operationLogEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doKickout(String s, Object o, String s1) {

    }

    @Override
    public void doReplaced(String s, Object o, String s1) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }
}
