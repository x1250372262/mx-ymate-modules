package com.mx.ymate.dev.support.mvc;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mx.ymate.dev.code.Code;
import com.mx.ymate.dev.code.ICode;
import com.mx.ymate.dev.support.mvc.i18n.I18nHelper;
import net.ymate.platform.commons.json.IJsonObjectWrapper;
import net.ymate.platform.webmvc.AbstractWebResult;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: mengxiang.
 * @create: 2021-07-02 16:58
 * @Description: 通用请求返回数据-操作消息提醒
 */
public class MxResult extends AbstractWebResult<String> {

    public static MxResult create() {
        return new MxResult().keepNullValue();
    }

    public static MxResult create(String code) {
        return new MxResult(code).keepNullValue();
    }


    public static MxResult create(ICode iCode) {
        return create(iCode.code()).msg(I18nHelper.getMsg(iCode.msg()));
    }


    public static MxResult ok() {
        return MxResult.create(Code.SUCCESS);
    }

    /**
     * /**
     * 失败result
     */
    public static MxResult fail() {
        return MxResult.create(Code.ERROR);
    }

    public static MxResult okData(Object data) {
        return ok().data(data);
    }

    public static MxResult failData(Object data) {
        return fail().data(data);
    }


    public static MxResult sameName() {
        String i18nMsg = I18nHelper.getMsg(Code.FIELDS_EXISTS.msg());
        return MxResult.create(Code.FIELDS_EXISTS.code()).msg(StrUtil.format(i18nMsg, "名称"));
    }

    public static MxResult sameData(String msg) {
        String i18nMsg = I18nHelper.getMsg(Code.FIELDS_EXISTS.msg());
        return MxResult.create(Code.FIELDS_EXISTS.code()).msg(StrUtil.format(i18nMsg, msg));
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


    public static boolean checkVersion(Object var1, Object var2) {
        return !Objects.equals(var1, var2);
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


    public static IView formatView(String path, String paramFormat, String defaultFormat, String paramCallback, IWebResult<?> result) {
        IView returnView = null;
        if (result != null) {
            HttpServletRequest request = WebContext.getRequest();
            if (WebUtils.isJsonAccepted(request, paramFormat) || StringUtils.equalsIgnoreCase(defaultFormat, Type.Const.FORMAT_JSON)) {
                returnView = result.withContentType().toJsonView(StringUtils.trimToNull(WebContext.getRequest().getParameter(paramCallback)));
            } else if (WebUtils.isXmlAccepted(request, paramFormat) || StringUtils.equalsIgnoreCase(defaultFormat, Type.Const.FORMAT_XML)) {
                returnView = result.withContentType().toXmlView();
            } else if (WebUtils.isAjax(request)) {
                returnView = result.withContentType().toJsonView(StringUtils.trimToNull(WebContext.getRequest().getParameter(paramCallback)));
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

    @Override
    public IJsonObjectWrapper toJsonObject() {
        IJsonObjectWrapper jsonObj = super.toJsonObject();
        if (jsonObj.has(Type.Const.PARAM_RET)) {
            Object ret = jsonObj.remove(Type.Const.PARAM_RET);
            if (ret != null) {
                jsonObj.put("code", ret);
            }
        }
        return jsonObj;
    }

    public String toJson() {
        return toJsonObject().toString(true, true);
    }

}
