package com.mx.ymate.security.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.support.ip2region.IpRegionBean;
import com.mx.ymate.dev.support.ip2region.IpRegionUtil;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.enums.OperationType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.event.OperationLogEvent;
import com.mx.ymate.security.web.controller.SecurityLoginController;
import com.mx.ymate.security.web.dao.ISecurityUserDao;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.NetworkUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.Bean;
import net.ymate.platform.core.beans.annotation.Inject;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.mx.ymate.security.base.config.SecurityConstants.LOG_EVENT_KEY;

/**
 * @Author: mengxiang.
 * @create: 2021-10-22 16:52
 * @Description: 登录退出监听
 */
@Bean
public class MxSaTokenListener implements SaTokenListener {

    private final ISecurityConfig config = Security.get().getConfig();

    @Inject
    private ISecurityUserDao iSecurityUserDao;


    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        if (config.openLog()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", Code.SUCCESS.code());
            jsonObject.put("msg", "退出成功");
            try {
                SecurityUser securityUser = iSecurityUserDao.findById(Convert.toStr(loginId));
                HttpServletRequest request = WebContext.getRequest();
                String userAgentStr = request.getHeader("user-agent");
                // *========数据库日志=========*//
                SecurityOperationLog securityOperationLog = SecurityOperationLog.builder()
                        .id(UUIDUtils.UUID())
                        .resourceId(securityUser.getResourceId())
                        .title("管理员退出")
                        .client(config.client())
                        .type(OperationType.LOGIN.name())
                        .typeName(OperationType.LOGIN.value())
                        .userId(Convert.toStr(loginId))
                        .userName(StringUtils.defaultIfBlank(securityUser.getRealName(), securityUser.getUserName()))
                        .createTime(DateTimeUtils.currentTimeMillis())
                        .requestUrl(request.getRequestURI())
                        .requestParam(JSONObject.toJSONString(ServletUtil.getParams(request)))
                        .returnCode(jsonObject.getString("code"))
                        .returnMessage(jsonObject.getString("msg"))
                        .returnResult(jsonObject.toJSONString())
                        .className(SecurityLoginController.class.getName())
                        .methodName("logout")
                        .os(UserAgentUtil.parse(userAgentStr).getOs().toString())
                        .browser(UserAgentUtil.parse(userAgentStr).getBrowser().toString())
                        .build();

                String ip = ServletUtil.getClientIP(request);
                if (StringUtils.isNotBlank(ip)) {
                    securityOperationLog.setIp(ip);
                    if (NetworkUtils.IP.isIPv4(ip) && !NetUtil.isInnerIP(ip)) {
                        IpRegionBean ipRegionBean = IpRegionUtil.parse(ip);
                        securityOperationLog.setLocation(ipRegionBean.getCountry() + ipRegionBean.getProvince() + ipRegionBean.getCity() + ipRegionBean.getIsp());
                    } else {
                        securityOperationLog.setLocation("");
                    }
                }
                // 保存数据库
                // 创建事件对象
                OperationLogEvent operationLogEvent = new OperationLogEvent(YMP.get(), OperationLogEvent.EVENT.CREATE_LOG);
                // 为当前事件设置扩展参数
                operationLogEvent.addParamExtend(LOG_EVENT_KEY, securityOperationLog);
                // 触发事件
                YMP.get().getEvents().fireEvent(operationLogEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
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
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {

    }


    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }

    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }
}
