package com.mx.ymate.dev.support.mvc;

import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.core.support.ErrorCode;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.webmvc.*;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.exception.ValidationResultException;
import net.ymate.platform.webmvc.impl.DefaultWebErrorProcessor;
import net.ymate.platform.webmvc.util.ExceptionProcessHelper;
import net.ymate.platform.webmvc.util.IExceptionProcessor;
import net.ymate.platform.webmvc.util.WebErrorCode;
import net.ymate.platform.webmvc.util.WebUtils;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: mengxiang.
 * @create: 2023-08-03 16:58
 * @Description: 自定义错误处理器
 */
public class MxWebErrorProcessor extends AbstractResponseErrorProcessor implements IWebErrorProcessor, IWebInitialization {

    private static final Log LOG = LogFactory.getLog(MxWebErrorProcessor.class);

    private IWebMvc owner;

    private boolean initialized;

    @Override
    public void initialize(IWebMvc owner) throws Exception {
        if (!initialized) {
            this.owner = owner;
            this.initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public IView showErrorMsg(IWebMvc owner, String code, String msg, Map<String, Object> dataMap) {
        doProcessErrorStatusCodeIfNeed(owner);
        HttpServletRequest httpServletRequest = WebContext.getRequest();
        String viewFormat = getErrorDefaultViewFormat(owner);
        if (WebUtils.isAjax(httpServletRequest) || WebUtils.isXmlFormat(httpServletRequest) || WebUtils.isJsonFormat(httpServletRequest) || StringUtils.containsAny(viewFormat, Type.Const.FORMAT_JSON, Type.Const.FORMAT_XML)) {
            return MxResult.formatView(null, Type.Const.PARAM_FORMAT, viewFormat, Type.Const.PARAM_CALLBACK, MxResult.create().code(code).msg(msg).data(dataMap));
        }
        return WebUtils.buildErrorView(owner, null, code, msg, null, 0, dataMap);
    }

    @Override
    public void onError(IWebMvc owner, Throwable e) {
        try {
            IView resultView = processError(owner, e);
            if (resultView != null) {
                resultView.render();
            }
        } catch (Exception e1) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(StringUtils.EMPTY, RuntimeUtils.unwrapThrow(e1));
            }
        }
    }

    @Override
    public IView onValidation(IWebMvc owner, Map<String, ValidateResult> results) {
        return showValidationResults(owner, results);
    }

    @Override
    public IView processError(IWebMvc owner, Throwable e) {
        IView returnView = null;
        try {
            Throwable unwrapThrow = RuntimeUtils.unwrapThrow(e);
            if (unwrapThrow != null) {
                if (unwrapThrow instanceof ValidationResultException) {
                    ValidationResultException exception = (ValidationResultException) unwrapThrow;
                    if (exception.getValidateResults() != null && !exception.getValidateResults().isEmpty()) {
                        returnView = showValidationResults(owner, exception.getValidateResults());
                    } else if (exception.getResultView() != null) {
                        returnView = exception.getResultView();
                        doProcessErrorStatusCodeIfNeed(owner);
                    } else {
                        returnView = View.httpStatusView(exception.getHttpStatus(), exception.getMessage());
                        doProcessErrorStatusCodeIfNeed(owner);
                    }
                } else {
                    IExceptionProcessor exceptionProcessor = ExceptionProcessHelper.DEFAULT.bind(unwrapThrow.getClass());
                    if (exceptionProcessor != null) {
                        IExceptionProcessor.Result result = exceptionProcessor.process(unwrapThrow);
                        if (result != null) {
                            String msg =  I18nHelper.getMsg(result.getCode(),result.getMessage());
                            returnView = showErrorMsg(owner, result.getCode(), msg, result.getAttributes());
                        } else {
                            doProcessError(owner, unwrapThrow);
                        }
                    } else {
                        doProcessError(owner, unwrapThrow);
                        returnView = showErrorMsg(owner, String.valueOf(ErrorCode.INTERNAL_SYSTEM_ERROR), I18nHelper.getMsg(BlurObject.bind(ErrorCode.INTERNAL_SYSTEM_ERROR).toStringValue(), ErrorCode.MSG_INTERNAL_SYSTEM_ERROR), null);
                    }
                    doProcessErrorStatusCodeIfNeed(owner);
                }
            }
        } catch (Exception e1) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(StringUtils.EMPTY, RuntimeUtils.unwrapThrow(e1));
            }
        }
        return returnView;
    }

    @Override
    public IView showValidationResults(IWebMvc owner, Map<String, ValidateResult> results) {
        String message = I18nHelper.getMsg(Code.FIELDS_EXISTS.msg(),Code.INVALID_PARAMETER.msg());
        HttpServletRequest httpServletRequest = WebContext.getRequest();
        if (!WebUtils.isAjax(httpServletRequest) && !WebUtils.isXmlFormat(httpServletRequest) && !WebUtils.isJsonFormat(httpServletRequest) && !StringUtils.containsAny(getErrorDefaultViewFormat(owner), Type.Const.FORMAT_JSON, Type.Const.FORMAT_XML)) {
            // 拼装所有的验证消息
            message = WebUtils.messageWithTemplate(owner.getOwner(), message, results.values());
        }
        Map<String, Object> dataMap = results.values().stream().collect(Collectors.toMap(ValidateResult::getName, ValidateResult::getMsg, (a, b) -> b, () -> new HashMap<>(results.size())));
        return showErrorMsg(owner, String.valueOf(Code.INVALID_PARAMETER.code()), message, dataMap);
    }

    @Override
    public IView onConvention(IWebMvc owner, IRequestContext requestContext) throws Exception {
        return null;
    }

    // ----------

    public final IWebMvc getOwner() {
        return owner;
    }

    public final String getErrorDefaultViewFormat() {
        return getErrorDefaultViewFormat(owner);
    }

    public boolean isErrorWithStatusCode() {
        return isErrorWithStatusCode(owner);
    }

    public final boolean isAnalysisDisabled() {
        return isAnalysisDisabled(owner);
    }
}
