package com.mx.ymate.netty.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import com.mx.ymate.netty.annotation.NettyHandlerDefine;
import com.mx.ymate.netty.bean.HandlerConfig;
import io.netty.channel.ChannelHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: xujianpeng.
 * @Date 2025/6/4.
 * @Time: 16:12.
 * @Description: Netty Handler 扫描工具类，支持注解扫描和手动注册混合使用
 */
public class NettyHandlerUtil {

    private static final Log LOG = LogFactory.getLog(NettyHandlerUtil.class);

    /**
     * 获取所有 Handler 配置项：自动 + 手动注册，排序合并返回
     */
    public static List<HandlerConfig> findAllHandlerConfig(List<String> packageList, IHandlerRegistrar handlerRegistrar) {
        // 自动扫描注解标记的 Handler
        List<HandlerConfig> allConfigs = new ArrayList<>(scanHandlersByAnnotation(packageList));
        // 手动注册的 Handler
        if (handlerRegistrar != null) {
            List<HandlerConfig> manualConfigs = handlerRegistrar.registerHandlers();
            if (manualConfigs != null) {
                allConfigs.addAll(manualConfigs);
            }
        }
        return allConfigs;
    }

    /**
     * 扫描 @NettyHandlerDefine 注解的类，构建 HandlerConfig
     */
    private static List<HandlerConfig> scanHandlersByAnnotation(List<String> packageList) {
        List<HandlerConfig> result = new ArrayList<>();
        Set<Class<?>> classList = new HashSet<>();
        // 扫描所有继承 ChannelHandler 的类
        for (String packageName : packageList) {
            Set<Class<?>> classSet = ClassUtil.scanPackageBySuper(packageName, ChannelHandler.class);
            classList.addAll(classSet);
        }
        for (Class<?> clazz : classList) {
            try {
                // 判断是否为 ChannelHandler 子类
                if (!ChannelHandler.class.isAssignableFrom(clazz)) {
                    LOG.warn("跳过非 ChannelHandler 类: " + clazz.getName());
                    continue;
                }
                // 获取注解
                NettyHandlerDefine annotation = AnnotationUtil.getAnnotation(clazz, NettyHandlerDefine.class);
                if (annotation == null) {
                    LOG.warn("未找到 @NettyHandlerDefine 注解: " + clazz.getName());
                    continue;
                }
                @SuppressWarnings("unchecked")
                Class<? extends ChannelHandler> handlerClass = (Class<? extends ChannelHandler>) clazz;
                // 构建配置对象
                HandlerConfig config = new HandlerConfig(handlerClass, annotation.order(), annotation.sharable());
                result.add(config);
            } catch (Throwable e) {
                LOG.error("处理类出错: " + clazz.getName(), e);
            }
        }
        return result;
    }
}
