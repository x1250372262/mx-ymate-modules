package com.mx.ymate.dev.support.mvc.i18n;

import com.mx.ymate.dev.support.event.IYmpInitializer;
import com.mx.ymate.dev.support.event.annotation.Initializer;
import net.ymate.platform.core.ApplicationEvent;

/**
 * @Author: xujianpeng.
 * @Date 2025/4/8.
 * @Time: 10:47.
 * @Description:
 */
@Initializer(value = -1000)
public class I18nInitializer implements IYmpInitializer {

    @Override
    public void startup(ApplicationEvent applicationEvent) {

    }

    @Override
    public void initialized(ApplicationEvent applicationEvent) {
        I18nHelper.init();
    }

    @Override
    public void destroyed(ApplicationEvent applicationEvent) {

    }
}
