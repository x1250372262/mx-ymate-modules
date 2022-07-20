package com.mx.ymate.dev.result;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.code.ICode;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.support.ErrorCode;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.IWebResult;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.WebUtils;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import net.ymate.platform.webmvc.view.impl.JspView;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 通用请求返回数据-操作消息提醒
 */
public class MxResult extends MxAbstractWebResult<String> implements Serializable {


    public static IMxResultBuilder builder() {
        IMxResultBuilder builder = null;
        try {
            builder = ClassUtils.getExtensionLoader(IMxResultBuilder.class).getExtensionClass().newInstance();
        } catch (Exception ignored) {
        }
        return builder != null ? builder : new DefaultMxResultBuilder();
    }

    public static IMxResultBuilder builder(ErrorCode errorCode) {
        return builder(WebContext.getContext().getOwner(), null, errorCode);
    }

    public static IMxResultBuilder builder(String resourceName, ErrorCode errorCode) {
        return builder(WebContext.getContext().getOwner(), resourceName, errorCode);
    }

    public static IMxResultBuilder builder(IWebMvc owner, ErrorCode errorCode) {
        return builder(owner, null, errorCode);
    }

    public static IMxResultBuilder builder(IWebMvc owner, String resourceName, ErrorCode errorCode) {
        IMxResultBuilder builder = builder();
        String msg = null;
        if (StringUtils.isNotBlank(errorCode.i18nKey())) {
            msg = WebUtils.i18nStr(owner, resourceName, errorCode.i18nKey(), null);
        }
        if (StringUtils.isBlank(msg)) {
            msg = WebUtils.errorCodeI18n(owner, resourceName, errorCode.code(), errorCode.message());
        }
        builder.code(errorCode.code()).msg(msg);
        if (!errorCode.attrs().isEmpty()) {
            builder.attrs(errorCode.attrs());
        }
        if (!errorCode.data().isEmpty()) {
            builder.data(errorCode.data());
        }
        return builder;
    }


    public static MxResult create() {
        return new MxResult().keepNullValue();
    }

    public static MxResult create(String code) {
        return new MxResult(code).keepNullValue();
    }


    public static MxResult create(ICode iCode) {
        return create(iCode.code()).msg(iCode.msg());
    }


    public static MxResult ok() {
        return MxResult.create(Code.SUCCESS.code());
    }

    /**
     * /**
     * 失败result
     */
    public static MxResult fail() {
        return MxResult.create(Code.ERROR);
    }


    public static MxResult sameName() {
        return MxResult.create(Code.FIELDS_EXISTS.code()).msg(StrUtil.format(Code.FIELDS_EXISTS.msg(), "名称"));
    }

    public static MxResult sameData(String msg) {
        return MxResult.create(Code.FIELDS_EXISTS.code()).msg(StrUtil.format(Code.FIELDS_EXISTS.msg(), msg));
    }

    public static MxResult noVersion() {
        return MxResult.create(Code.VERSION_NOT_SAME);
    }

    public static MxResult noData() {
        return MxResult.create(Code.NO_DATA);
    }

    public static MxResult noLogin() {
        return MxResult.create(Code.NOT_LOGIN);
    }

    /**
     * 根据参数返回成功还是失败
     */
    public static MxResult result(Object object) {
        if (object != null) {
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    /**
     * 根据参数返回成功还是失败
     */
    public static MxResult result(int result) {
        if (result >= 1) {
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    /**
     * 根据参数返回成功还是失败
     */
    public static MxResult result(List<?> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    /**
     * 根据参数返回成功还是失败
     */
    public static MxResult result(int[] result) {
        if (result != null && result.length > 0) {
            return MxResult.ok();
        }
        return MxResult.fail();
    }

    public String toJson() {
        return JSONObject.toJSONString(this, SerializerFeature.PrettyFormat);
    }

    public static boolean checkVersion(Object var1, Object var2) {
        return Objects.equals(var1, var2);
    }

    public MxResult() {
        super();
    }

    public MxResult(String code) {
        super(code);
    }

    @Override
    public boolean isSuccess() {
        return Code.SUCCESS.code().equals(code());
    }


    public static IView formatView(IMxWebResult<?> result) {
        return formatView(null, Type.Const.PARAM_FORMAT, Type.Const.PARAM_CALLBACK, result);
    }

    public static IView formatView(String path, IMxWebResult<?> result) {
        return formatView(path, Type.Const.PARAM_FORMAT, Type.Const.PARAM_CALLBACK, result);
    }

    public static IView formatView(IMxWebResult<?> result, String defaultFormat) {
        return formatView(null, Type.Const.PARAM_FORMAT, defaultFormat, Type.Const.PARAM_CALLBACK, result);
    }

    /**
     * @param path          JSP模块路径
     * @param paramFormat   数据格式，可选值：json|jsonp|xml
     * @param paramCallback 当数据结式为jsonp时，指定回调方法参数名
     * @param result        回应的数据对象
     * @return 根据paramFormat等参数判断返回对应的视图对象
     */
    public static IView formatView(String path, String paramFormat, String paramCallback, IMxWebResult<?> result) {
        return formatView(path, paramFormat, null, paramCallback, result);
    }

    public static IView formatView(String path, String paramFormat, String defaultFormat, String paramCallback, IMxWebResult<?> result) {
        IView returnView = null;
        if (result != null) {
            HttpServletRequest request = WebContext.getRequest();
            if (WebUtils.isJsonAccepted(request, paramFormat) || StringUtils.equalsIgnoreCase(defaultFormat, Type.Const.FORMAT_JSON)) {
                returnView = result.withContentType().toMxJsonView(StringUtils.trimToNull(WebContext.getRequest().getParameter(paramCallback)));
            } else if (WebUtils.isXmlAccepted(request, paramFormat) || StringUtils.equalsIgnoreCase(defaultFormat, Type.Const.FORMAT_XML)) {
                returnView = result.withContentType().toXmlView();
            } else if (WebUtils.isAjax(request)) {
                returnView = result.withContentType().toMxJsonView(StringUtils.trimToNull(WebContext.getRequest().getParameter(paramCallback)));
            }
        }
        if (returnView == null) {
            if (StringUtils.isNotBlank(path)) {
                returnView = new JspView(path);
                if (result != null) {
                    returnView.addAttribute(Type.Const.PARAM_RET, result.code());
                    //
                    if (StringUtils.isNotBlank(result.msg())) {
                        returnView.addAttribute(Type.Const.PARAM_MSG, result.msg());
                    }
                    if (result.data() != null) {
                        returnView.addAttribute(Type.Const.PARAM_DATA, result.data());
                    }
                    for (Map.Entry<String, Object> entry : result.attrs().entrySet()) {
                        returnView.addAttribute(entry.getKey(), entry.getValue());
                    }
                }
            } else if (result != null && StringUtils.isNotBlank(result.msg())) {
                returnView = new HttpStatusView(HttpServletResponse.SC_BAD_REQUEST, result.msg());
            } else {
                returnView = new HttpStatusView(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        return returnView;
    }

    @Override
    public MxResult code(String code) {
        super.code(code);
        return this;
    }

    @Override
    public MxResult msg(String msg) {
        super.msg(msg);
        return this;
    }

    @Override
    public MxResult data(Object data) {
        super.data(data);
        return this;
    }

    @Override
    public MxResult attrs(Map<String, Object> attrs) {
        super.attrs(attrs);
        return this;
    }

    @Override
    public MxResult dataAttr(String dataKey, Object dataValue) {
        super.dataAttr(dataKey, dataValue);
        return this;
    }

    @Override
    public MxResult attr(String attrKey, Object attrValue) {
        super.attr(attrKey, attrValue);
        return this;
    }

    @Override
    public MxResult withContentType() {
        super.withContentType();
        return this;
    }

    @Override
    public MxResult keepNullValue() {
        super.keepNullValue();
        return this;
    }

    @Override
    public MxResult snakeCase() {
        super.snakeCase();
        return this;
    }


}
