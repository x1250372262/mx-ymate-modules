package com.mx.ymate.dev.support.spi;

import com.mx.ymate.dev.support.spi.annotation.SpiInject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.IBeanFactory;
import net.ymate.platform.core.beans.IBeanInjector;
import net.ymate.platform.core.beans.annotation.Injector;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * @author 徐建鹏
 * @Date .
 * @Time: .
 * @Description: 动态配置rpc
 */
@Injector(SpiInject.class)
public class SpiInjector implements IBeanInjector {

    @Override
    public Object inject(IBeanFactory beanFactory, Annotation annotation, Class<?> targetClass, Field field, Object originInject) {
        //返回的值
        Object obj = null;
        SpiInject spiInject = field.getAnnotation(SpiInject.class);
        if (spiInject != null) {
            List<? extends Class<?>> spiClassList;
            String className = spiInject.name();
            try {
                spiClassList = ClassUtils.getExtensionLoader(targetClass).getExtensionClasses();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (spiClassList == null) {
                return originInject;
            }
            Class<?> clazz;
            if (StringUtils.isBlank(className)) {
                clazz = spiClassList.get(0);
            } else {
                Optional<? extends Class<?>> optionalClass = spiClassList.stream().filter(spiClass -> className.equals(spiClass.getName())).findAny();
                if (optionalClass.isPresent()) {
                    clazz = optionalClass.get();
                } else {
                    clazz = spiClassList.get(0);
                }
            }
            obj = YMP.get().getBeanFactory().getBean(clazz);
        }
        if (obj == null) {
            obj = originInject;
        }
        return obj;
    }
}
