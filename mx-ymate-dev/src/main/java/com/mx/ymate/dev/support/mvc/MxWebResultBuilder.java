package com.mx.ymate.dev.support.mvc;

import com.mx.ymate.dev.code.Code;
import net.ymate.platform.commons.json.IJsonObjectWrapper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.webmvc.IWebResult;
import net.ymate.platform.webmvc.IWebResultBuilder;
import net.ymate.platform.webmvc.base.Type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 自定义结果构建
 */
public class MxWebResultBuilder implements IWebResultBuilder {

    private final MxResult result = new MxResult();

    @Override
    public IWebResultBuilder fromJson(IJsonObjectWrapper jsonObject) {
        Map<String, Object> map = new HashMap<>(jsonObject.toMap());
        Object ret = map.remove(Type.Const.PARAM_RET);
        if (ret != null) {
            result.code(BlurObject.bind(ret).toStringValue());
        }
        Object msg = map.remove(Type.Const.PARAM_MSG);
        if (msg != null) {
            result.msg(BlurObject.bind(msg).toStringValue());
        }
        Object data = map.remove(Type.Const.PARAM_DATA);
        if (data != null) {
            result.data(data);
        }
        result.attrs(map);
        return this;
    }

    @Override
    public IWebResultBuilder succeed() {
        result.code(Code.SUCCESS.code());
        return this;
    }

    @Override
    public IWebResultBuilder code(Serializable code) {
        result.code(code.toString());
        return this;
    }

    @Override
    public IWebResultBuilder msg(String msg) {
        result.msg(msg);
        return this;
    }

    @Override
    public IWebResultBuilder data(Object data) {
        result.data(data);
        return this;
    }

    @Override
    public IWebResultBuilder attrs(Map<String, Object> attrs) {
        result.attrs(attrs);
        return this;
    }

    @Override
    public IWebResultBuilder dataAttr(String dataKey, Object dataValue) {
        result.dataAttr(dataKey, dataValue);
        return this;
    }

    @Override
    public IWebResultBuilder attr(String attrKey, Object attrValue) {
        result.attr(attrKey, attrValue);
        return this;
    }

    @Override
    public IWebResult<?> build() {
        return result;
    }
}
