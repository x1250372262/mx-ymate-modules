package com.mx.ymate.dev.view;

import net.ymate.platform.commons.json.JsonWrapper;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.AbstractView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @Author: 徐建鹏.
 * @create: 2022-07-11 13:36
 * @Description:
 */
public class MxJsonView extends AbstractView {

    private final Object jsonObj;

    private boolean withContentType;

    private String jsonCallback;

    private boolean keepNullValue;

    private boolean snakeCase;

    public static MxJsonView bind(Object obj) {
        if (obj instanceof String) {
            return new MxJsonView((String) obj);
        } else {
            return new MxJsonView(obj);
        }
    }

    /**
     * 构造器
     *
     * @param obj 任意对象
     */
    public MxJsonView(Object obj) {
        jsonObj = JsonWrapper.toJson(obj);
    }

    /**
     * 构造器
     *
     * @param jsonStr JSON字符串
     */
    public MxJsonView(String jsonStr) {
        jsonObj = JsonWrapper.fromJson(jsonStr);
    }

    /**
     * @return 设置ContentType为"application/json"或"text/javascript"，默认为空
     */
    public MxJsonView withContentType() {
        withContentType = true;
        return doSetContentType();
    }

    public Object getJsonObj() {
        return jsonObj;
    }

    private MxJsonView doSetContentType() {
        if (withContentType) {
            if (jsonCallback == null) {
                setContentType(Type.ContentType.JSON.getContentType());
            } else {
                setContentType(Type.ContentType.JAVASCRIPT.getContentType());
            }
        }
        return this;
    }

    /**
     * @param callback 回调方法名称
     * @return 设置并采用JSONP方式输出，回调方法名称由参数callback指定，若callback参数无效则不启用
     */
    public MxJsonView withJsonCallback(String callback) {
        jsonCallback = StringUtils.trimToNull(callback);
        return doSetContentType();
    }

    /**
     * @return 设置是否保留空值属性
     */
    public MxJsonView keepNullValue() {
        keepNullValue = true;
        return this;
    }

    public MxJsonView snakeCase() {
        snakeCase = true;
        return this;
    }

    @Override
    protected void doRenderView() throws Exception {
        StringBuilder jsonStringBuilder = new StringBuilder(JsonWrapper.toJsonString(jsonObj, false, keepNullValue, snakeCase));
        if (jsonCallback != null) {
            jsonStringBuilder.insert(0, jsonCallback + "(").append(");");
        }
        HttpServletResponse httpServletResponse = WebContext.getResponse();
        IOUtils.write(jsonStringBuilder.toString(), httpServletResponse.getOutputStream(), httpServletResponse.getCharacterEncoding());
    }

    @Override
    public void render(OutputStream output) throws Exception {
        StringBuilder jsonStringBuilder = new StringBuilder(JsonWrapper.toJsonString(jsonObj, false, keepNullValue, snakeCase));
        if (jsonCallback != null) {
            jsonStringBuilder.insert(0, jsonCallback + "(").append(");");
        }
        IOUtils.write(jsonStringBuilder, output, WebContext.getResponse().getCharacterEncoding());
    }
}
