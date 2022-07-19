/*
 * Copyright 2007-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mx.ymate.dev.result;

import com.mx.ymate.dev.code.Code;
import net.ymate.platform.commons.json.IJsonObjectWrapper;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.webmvc.base.Type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2020/09/08 22:31
 * @since 2.1.0
 */
public class DefaultMxResultBuilder implements IMxResultBuilder {

    private final MxResult result = new MxResult();

    @Override
    public IMxResultBuilder fromJson(IJsonObjectWrapper jsonObject) {
        Map<String, Object> map = new HashMap<>(jsonObject.toMap());
        Object code = map.remove("code");
        if (code != null) {
            result.code(BlurObject.bind(code).toStringValue());
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
    public IMxResultBuilder succeed() {
        result.code(Code.SUCCESS.code());
        return this;
    }

    @Override
    public IMxResultBuilder code(Serializable code) {
        result.code(BlurObject.bind(code).toStringValue());
        return this;
    }

    @Override
    public IMxResultBuilder msg(String msg) {
        result.msg(msg);
        return this;
    }

    @Override
    public IMxResultBuilder data(Object data) {
        result.data(data);
        return this;
    }

    @Override
    public IMxResultBuilder attrs(Map<String, Object> attrs) {
        result.attrs(attrs);
        return this;
    }

    @Override
    public IMxResultBuilder dataAttr(String dataKey, Object dataValue) {
        result.dataAttr(dataKey, dataValue);
        return this;
    }

    @Override
    public IMxResultBuilder attr(String attrKey, Object attrValue) {
        result.attr(attrKey, attrValue);
        return this;
    }

    @Override
    public IMxWebResult<?> build() {
        return result;
    }
}
