package com.mx.ymate.qwen;

import cn.hutool.core.collection.CollUtil;
import com.mx.ymate.qwen.bean.MethodBean;
import com.mx.ymate.qwen.bean.ParameterBean;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author: xujianpeng.
 * @Date 2025/2/28.
 * @Time: 12:58.
 * @Description:
 */
public class ReflectionUtil {

    public static Object invokeMethod(MethodBean methodBean) throws Exception {
        List<ParameterBean> parameterList = methodBean.getParameters();
        if(CollUtil.isEmpty(parameterList)){
            return methodBean.getMethod().invoke(methodBean.getObject());
        }
        // 创建参数数组
        Object[] argsArray = new Object[parameterList.size()];
        // 填充参数数组
        for (int i = 0; i < parameterList.size(); i++) {
            String paramName =parameterList.get(i).getName();
            // 获取 Map 中的参数值并赋值到 argsArray
            argsArray[i] = methodBean.getParameterMap().get(paramName);
        }
        // 调用方法并返回结果
        return methodBean.getMethod().invoke(methodBean.getObject(), argsArray);
    }
}
