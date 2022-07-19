package com.mx.ymate.dev.result;

import net.ymate.platform.commons.json.IJsonObjectWrapper;
import net.ymate.platform.commons.json.JsonWrapper;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public interface IMxResultBuilder {

    default IMxResultBuilder fromJson(String jsonStr) {
        return fromJson(JsonWrapper.fromJson(jsonStr));
    }

    default IMxResultBuilder fromJson(JsonWrapper jsonWrapper) {
        if (jsonWrapper != null && jsonWrapper.isJsonObject()) {
            return fromJson(Objects.requireNonNull(jsonWrapper.getAsJsonObject()));
        }
        return this;
    }

    IMxResultBuilder fromJson(IJsonObjectWrapper jsonObject);

    IMxResultBuilder succeed();

    IMxResultBuilder code(Serializable code);

    IMxResultBuilder msg(String msg);

    IMxResultBuilder data(Object data);

    IMxResultBuilder attrs(Map<String, Object> attrs);

    IMxResultBuilder dataAttr(String dataKey, Object dataValue);

    IMxResultBuilder attr(String attrKey, Object attrValue);

    IMxWebResult<?> build();
}
