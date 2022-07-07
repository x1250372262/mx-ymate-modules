//package com.mx.ymate.satoken.ymate.event;
//
//import cn.dev33.satoken.exception.NotLoginException;
//import net.ymate.platform.commons.lang.BlurObject;
//import net.ymate.platform.core.ApplicationEvent;
//import net.ymate.platform.core.IApplication;
//import net.ymate.platform.core.IApplicationInitializer;
//import net.ymate.platform.core.event.Events;
//import net.ymate.platform.core.event.IEventListener;
//import net.ymate.platform.webmvc.util.ExceptionProcessHelper;
//import net.ymate.platform.webmvc.util.IExceptionProcessor;
//
//import static com.mx.ymate.dev.code.Code.NOT_LOGIN;
//import static net.ymate.platform.core.ApplicationEvent.EVENT.APPLICATION_INITIALIZED;
//
///**
// * @Author: 徐建鹏.
// * @create: 2022-07-03 11:50
// * @Description:
// */
//public class SaTokenExecptionInitializer implements IApplicationInitializer {
//
//    @Override
//    public void afterEventInit(IApplication application, Events events) {
//        // 订阅模块事件：默认同步
//        events.registerListener(ApplicationEvent.class, (IEventListener<ApplicationEvent>) context -> {
//            if (context.getEventName() == APPLICATION_INITIALIZED) {
//                ExceptionProcessHelper.DEFAULT.registerProcessor(NotLoginException.class, target -> new IExceptionProcessor.Result(BlurObject.bind(NOT_LOGIN.code()).toIntValue(), NOT_LOGIN.msg()));
//            }
//            return false;
//        });
//    }
//}
