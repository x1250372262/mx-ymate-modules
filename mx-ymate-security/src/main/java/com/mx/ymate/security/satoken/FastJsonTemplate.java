package com.mx.ymate.security.satoken;

import cn.dev33.satoken.json.SaJsonTemplate;
import net.ymate.platform.commons.json.JsonWrapper;

import java.util.Map;

/**
 * @Author: mengxiang.
 * @create: 2022-04-20 00:00
 * @Description: JSON 转换器
 */
public class FastJsonTemplate implements SaJsonTemplate {


    /**
     * 将任意对象转换为 json 字符串
     *
     * @param obj 对象
     * @return 转换后的 json 字符串
     */
    public String toJsonString(Object obj) {
        return JsonWrapper.toJsonString(obj, true, true);
    }

    /**
     * 将 json 字符串解析为 Map
     */
    @Override
    public Map<String, Object> parseJsonToMap(String jsonStr) {
        return JsonWrapper.fromJson(jsonStr).getAsJsonObject().toMap();
    }

}
