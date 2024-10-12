package com.mx.ymate.security.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.support.ip2region.IpRegionBean;
import com.mx.ymate.dev.support.ip2region.IpRegionUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.base.annotation.OperationLog;
import com.mx.ymate.security.base.bean.LoginUser;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.event.OperationLogEvent;
import com.mx.ymate.security.handler.IUserHandler;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.NetworkUtils;
import net.ymate.platform.commons.util.UUIDUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.beans.intercept.InterceptException;
import net.ymate.platform.log.ILogger;
import net.ymate.platform.log.Logs;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static com.mx.ymate.security.base.config.SecurityConstants.LOG_EVENT_KEY;

/**
 * @Author: mengxiang.
 * @Date: 2024-10-11 17:00
 * @Description:
 */
public class OperationLogInterceptor extends AbstractInterceptor {

    private final ISecurityConfig securityConfig = Security.get().getConfig();

    private final ILogger iLogger = Logs.get().getLogger();

    public OperationLogInterceptor() {
    }

    @Override
    protected Object before(InterceptContext context) throws InterceptException {
        return null;
    }

    @Override
    protected Object after(InterceptContext context) throws InterceptException {
        if (securityConfig.openLog()) {
            handlerLog(context);
        }
        return null;
    }


    private void handlerLog(InterceptContext context) {
        try {
            // 获得注解
            Method method = context.getTargetMethod();
            if (method == null) {
                return;
            }
            OperationLog operationLog = method.getAnnotation(OperationLog.class);
            if (operationLog == null) {
                return;
            }
            LoginUser loginUser = SaUtil.user();
            if (loginUser == null) {
                return;
            }
            Object result = context.getResultObject();
            if (!(result instanceof MxResult)) {
                return;
            }
            MxResult mxResult = (MxResult) result;
            String code = mxResult.code();
            String msg = mxResult.msg();
            HttpServletRequest request = WebContext.getRequest();
            UserAgent userAgent = UserAgentUtil.parse(request.getHeader("user-agent"));
            // *========数据库日志=========*//
            IUserHandler userHandler = securityConfig.userHandlerClass();
            String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.LOG, null), securityConfig.client());
            SecurityOperationLog securityOperationLog = SecurityOperationLog.builder()
                    .id(UUIDUtils.UUID())
                    .title(operationLog.title())
                    .resourceId(resourceId)
                    .type(operationLog.operationType().name())
                    .typeName(operationLog.operationType().value())
                    .userId(loginUser.getId())
                    .userName(loginUser.getUserName())
                    .createTime(DateTimeUtils.currentTimeMillis())
                    .requestUrl(request.getRequestURI())
                    .requestParam(JSONObject.toJSONString(ServletUtil.getParams(request)))
                    .returnCode(Convert.toStr(code)).returnMessage(msg)
                    .returnResult(mxResult.toJson())
                    .className(context.getTargetClass().getName())
                    .methodName(method.getName())
                    .os(userAgent.getOs().toString())
                    .browser(userAgent.getBrowser().toString())
                    .client(securityConfig.client())
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
            // 记录本地异常日志
            iLogger.error("==日志记录异常==");
            iLogger.error("异常信息:{}", e);
            throw new RuntimeException(e);
        }
    }
}
