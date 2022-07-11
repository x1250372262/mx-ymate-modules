package com.mx.ymate.security.interceptor;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.view.MxJsonView;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.SaUtil;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.annotation.OperationLog;
import com.mx.ymate.security.base.enums.ResourceType;
import com.mx.ymate.security.base.model.SecurityOperationLog;
import com.mx.ymate.security.base.model.SecurityUser;
import com.mx.ymate.security.event.OperationLogEvent;
import com.mx.ymate.security.handler.IUserHandler;
import net.ymate.platform.commons.json.IJsonObjectWrapper;
import net.ymate.platform.commons.json.JsonWrapper;
import net.ymate.platform.commons.util.DateTimeUtils;
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

/**
 * 注解式鉴权 - 拦截器
 *
 * @author kong
 */
public class OperationLogInterceptor extends AbstractInterceptor {

    private final ISecurityConfig securityConfig = Security.get().getConfig();
    private final SaUtil saUtil = YMP.get().getBeanFactory().getBean(SaUtil.class);

    private final ILogger iLogger = Logs.get().getLogger();

    /**
     * 构建： 注解式鉴权 - 拦截器
     */
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
            SecurityUser securityUser = saUtil.user();
            if (securityUser == null) {
                return;
            }
            Object ret = context.getResultObject();
            Object result = ((MxJsonView) ret).getJsonObj();
            IJsonObjectWrapper jsonWrapper = JsonWrapper.toJson(result).getAsJsonObject();
            String code = StringUtils.defaultString(jsonWrapper.getString("code"),"未知");
            String msg = StringUtils.defaultString(jsonWrapper.getString("msg"), Code.SUCCESS.code().equals(code)?Code.SUCCESS.msg():"未知");
            HttpServletRequest request = WebContext.getRequest();
            String userAgentStr = request.getHeader("user-agent");
            // *========数据库日志=========*//
            IUserHandler userHandler = securityConfig.userHandlerClass();
            String resourceId = StringUtils.defaultIfBlank(userHandler.buildResourceId(ResourceType.LOG), securityConfig.client());
            SecurityOperationLog securityOperationLog = SecurityOperationLog.builder()
                    .id(UUIDUtils.UUID())
                    .title(operationLog.title())
                    .resourceId(resourceId)
                    .type(operationLog.operationType().name())
                    .typeName(operationLog.operationType().value())
                    .userId(securityUser.getId())
                    .userName(securityUser.getUserName())
                    .createTime(DateTimeUtils.currentTimeMillis())
                    .requestUrl(request.getRequestURI())
                    .requestParam(JSONObject.toJSONString(ServletUtil.getParams(request)))
                    .returnCode(Convert.toStr(code)).returnMessage(msg)
                    .returnResult(JSONObject.toJSONString(ret))
                    .className(context.getTargetClass().getName())
                    .methodName(method.getName())
                    .ip(ServletUtil.getClientIP(request))
                    //位置之后再说
                    .location("")
                    .os(UserAgentUtil.parse(userAgentStr).getOs().toString())
                    .browser(UserAgentUtil.parse(userAgentStr).getBrowser().toString())
                    .client(securityConfig.client())
                    .build();
            // 保存数据库
            // 创建事件对象
            OperationLogEvent operationLogEvent = new OperationLogEvent(YMP.get(), OperationLogEvent.EVENT.CREATE_LOG);
            // 为当前事件设置扩展参数
            operationLogEvent.addParamExtend("log", securityOperationLog);
            // 触发事件
            YMP.get().getEvents().fireEvent(operationLogEvent);
        } catch (Exception exp) {
            // 记录本地异常日志
            iLogger.error("==日志记录异常==");
            iLogger.error("异常信息:{}", exp);
            exp.printStackTrace();
        }
    }
}
