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
import net.ymate.platform.webmvc.IWebResultBuilder;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.impl.DefaultWebResultBuilder;
import net.ymate.platform.webmvc.util.WebUtils;
import org.apache.commons.lang3.StringUtils;

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


    public static IWebResultBuilder builder() {
        IWebResultBuilder builder = null;
        try {
            builder = ClassUtils.getExtensionLoader(IWebResultBuilder.class).getExtensionClass().newInstance();
        } catch (Exception ignored) {
        }
        return builder != null ? builder : new DefaultWebResultBuilder();
    }

    public static IWebResultBuilder builder(ErrorCode errorCode) {
        return builder(WebContext.getContext().getOwner(), null, errorCode);
    }

    public static IWebResultBuilder builder(String resourceName, ErrorCode errorCode) {
        return builder(WebContext.getContext().getOwner(), resourceName, errorCode);
    }

    public static IWebResultBuilder builder(IWebMvc owner, ErrorCode errorCode) {
        return builder(owner, null, errorCode);
    }

    public static IWebResultBuilder builder(IWebMvc owner, String resourceName, ErrorCode errorCode) {
        IWebResultBuilder builder = builder();
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
        return new MxResult();
    }

    public static MxResult create(String code) {
        return new MxResult(code);
    }


    public static MxResult create(ICode iCode) {
        return create(iCode.code()).msg(iCode.msg());
    }


    public static MxResult ok() {
        return new MxResult(Code.SUCCESS.code());
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
