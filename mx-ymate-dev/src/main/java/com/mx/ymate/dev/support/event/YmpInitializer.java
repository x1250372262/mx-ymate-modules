package com.mx.ymate.dev.support.event;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ClassUtil;
import com.mx.ymate.dev.support.event.annotation.Initializer;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.ApplicationEvent;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.event.Events;
import net.ymate.platform.core.event.IEventListener;
import net.ymate.platform.core.event.IEventRegister;
import net.ymate.platform.core.event.annotation.EventRegister;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @Author: mengxiang.
 * @create: 2025-02-25
 * @Description: 框架初始化成功事件监听  只支持异步
 */
@EventRegister
public class YmpInitializer implements IEventRegister {

    private static final Log LOG = LogFactory.getLog(YmpInitializer.class);

    private static final String PACKAGES = YMP.get().getParam("mx.initializer.packages");

    private final List<InitializerBean> initializerBeanList = new ArrayList<>();


    public YmpInitializer() {
        LOG.info("初始化启动事件");
        List<Class<?>> classList = findClass();
        // 遍历所有的类
        for (Class<?> clazz : classList) {
            // 检查每个类是否有 @Initializer 注解
            Initializer initializer = AnnotationUtil.getAnnotation(clazz, Initializer.class);
            if (initializer == null) {
                LOG.warn("class:" + clazz.getName() + "没有Initializer注解，请检查");
                continue;
            }
            IYmpInitializer ympInitializer = ClassUtils.impl(clazz, IYmpInitializer.class);
            if (ympInitializer == null) {
                LOG.error("class:" + clazz.getName() + "实例化失败");
                continue;
            }
            initializerBeanList.add(new InitializerBean(initializer.value(), ympInitializer));
            if (initializerBeanList.size() > 1) {
                initializerBeanList.sort(Comparator.comparingInt(InitializerBean::getOrder));
            }
        }
    }

    private List<Class<?>> findClass() {
        List<Class<?>> classList = new ArrayList<>();
        List<String> packageList = ListUtil.toList("com.mx");
        if (StringUtils.isNotBlank(PACKAGES)) {
            String[] packageArr = PACKAGES.split("\\|");
            if (packageArr.length > 0) {
                packageList.addAll(Arrays.asList(packageArr));
            }
        }
        for (String packageName : packageList) {
            Set<Class<?>> classSet = ClassUtil.scanPackageBySuper(packageName, IYmpInitializer.class);
            classList.addAll(classSet);
        }
        return classList;
    }

    @Override
    public void register(Events events) throws Exception {
        // 订阅模块事件：异步
        events.registerListener(Events.MODE.ASYNC, ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
            for (InitializerBean initializerBean : initializerBeanList) {
                IYmpInitializer ympInitializer = initializerBean.getYmpInitializer();
                switch (context.getEventName()) {
                    case APPLICATION_STARTUP:
                        ympInitializer.startup(context);
                        break;
                    case APPLICATION_INITIALIZED:
                        ympInitializer.initialized(context);
                        break;
                    case APPLICATION_DESTROYED:
                        ympInitializer.destroyed(context);
                        break;
                    default:
                        LOG.error("未知事件 " + context.getEventName());
                        break;
                }

            }
            return false;
        });
    }
}
