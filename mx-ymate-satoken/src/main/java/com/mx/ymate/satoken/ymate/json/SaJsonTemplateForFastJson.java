package com.mx.ymate.satoken.ymate.json;

import cn.dev33.satoken.json.SaJsonTemplate;
import net.ymate.platform.commons.json.JsonWrapper;

import java.util.Map;

/**
 * JSON 转换器， Jackson 版实现
 */
public class SaJsonTemplateForFastJson implements SaJsonTemplate {


    /**
     * 将任意对象转换为 json 字符串
     *
     * @param obj 对象
     * @return 转换后的 json 字符串
     */
    public String toJsonString(Object obj) {
        return JsonWrapper.toJsonString(obj);
    }

    /**
     * 将 json 字符串解析为 Map
     */
    @Override
    public Map<String, Object> parseJsonToMap(String jsonStr) {
        return JsonWrapper.fromJson(jsonStr).getAsJsonObject().toMap();
    }

}
