package com.mx.ymate.dev.util;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mx.ymate.dev.support.mvc.MxResult;
import net.ymate.platform.commons.http.CloseableHttpRequestBuilder;
import net.ymate.platform.commons.http.IHttpRequest;
import net.ymate.platform.commons.http.IHttpResponse;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.log.Logs;
import net.ymate.platform.webmvc.base.Type;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.mx.ymate.dev.code.Code.SERVER_REQUEST_ERROR;

/**
 * @Author: mengxiang.
 * @create: 2022-07-13 15:43
 * @Description:
 */
public class HttpUtil {

    public enum HttpMethod {

        /**
         * GET
         */
        GET,

        /**
         * POST
         */
        POST
    }


    public static MxResult request(String url, HttpMethod httpMethod, Map<String, String> params) throws Exception {

        IHttpRequest request = CloseableHttpRequestBuilder.create(url).addParams(params).build();
        MxResult result;
        try (IHttpResponse httpResponse = request.execute(httpMethod.name())) {
            if (httpResponse == null) {
                return MxResult.create(SERVER_REQUEST_ERROR);
            }
            if (httpResponse.getStatusCode() != HttpServletResponse.SC_OK) {
                return MxResult.create(SERVER_REQUEST_ERROR.code()).msg(httpResponse.toString());
            }
            result = create(JSON.parseObject(httpResponse.getContent()));
            if (!result.isSuccess()) {
                return MxResult.create(SERVER_REQUEST_ERROR.code()).msg(result.msg());
            }
            return result;
        } catch (Exception e) {
            Logs.get().getLogger().error("http调用失败", e);
            throw new RuntimeException(e);
        }
    }

    private static MxResult create(JSONObject jsonObject) {
        MxResult mxResult = MxResult.create();
        Object code = jsonObject.remove("code");
        if (code != null) {
            mxResult.code(BlurObject.bind(code).toStringValue());
        }
        Object msg = jsonObject.remove(Type.Const.PARAM_MSG);
        if (msg != null) {
            mxResult.msg(BlurObject.bind(msg).toStringValue());
        }
        Object data = jsonObject.remove(Type.Const.PARAM_DATA);
        if (data != null) {
            mxResult.data(data);
        }
        mxResult.attrs(jsonObject);
        return mxResult;
    }

    public static MxResult get(String url, Map<String, String> params) throws Exception {
        return request(url, HttpMethod.GET, params);
    }

    public static MxResult post(String url, Map<String, String> params) throws Exception {
        return request(url, HttpMethod.POST, params);
    }

    public static MxResult get(String url) throws Exception {
        return get(url, MapUtil.empty());
    }

    public static MxResult post(String url) throws Exception {
        return post(url, MapUtil.empty());
    }
}
