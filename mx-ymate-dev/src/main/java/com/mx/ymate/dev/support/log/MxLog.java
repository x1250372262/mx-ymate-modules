package com.mx.ymate.dev.support.log;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: mengxiang.
 * @create: 2022-03-25 16:43
 * @Description: 快捷打印日志
 */
public class MxLog {

    private static final MxSecurityManager MX_SECURITY_MANAGER = new MxSecurityManager();

    private MxLog() {
    }

    private static Logger create() {
        return LoggerFactory.getLogger(MX_SECURITY_MANAGER.getCallerClass().getName());
    }

    public static Logger create(String name) {
        return LoggerFactory.getLogger(name);
    }

    private static String buildMessage(String message) {
        Thread thread = Thread.currentThread();
        StackTraceElement[] stacks = thread.getStackTrace();
        String className = stacks[3].getClassName();
        int lineNumber = stacks[3].getLineNumber();
        String template = "{}.{} - " + message;
        return StrUtil.format(template, className, lineNumber);
    }

    public static void trace(String message) {
        create().trace(buildMessage(message));
    }

    public static void trace(String message, Object... objects) {
        create().trace(buildMessage(message), objects);
    }

    public static void trace(String message, Object obj) {
        create().trace(buildMessage(message), obj);
    }

    public static void trace(String message, Throwable throwable) {
        create().trace(buildMessage(message), throwable);
    }


    public static void debug(String message) {
        create().debug(buildMessage(message));
    }

    public static void debug(String message, Object... objects) {
        create().debug(buildMessage(message), objects);
    }

    public static void debug(String message, Object obj) {
        create().debug(buildMessage(message), obj);
    }

    public static void debug(String message, Throwable throwable) {
        create().debug(buildMessage(message), throwable);
    }

    public static void info(String message) {
        create().info(buildMessage(message));
    }

    public static void info(String message, Object... objects) {
        create().info(buildMessage(message), objects);
    }

    public static void info(String message, Object obj) {
        create().info(buildMessage(message), obj);
    }

    public static void info(String message, Throwable throwable) {
        create().info(buildMessage(message), throwable);
    }

    public static void warn(String message) {
        create().warn(buildMessage(message));
    }

    public static void warn(String message, Object... objects) {
        create().warn(buildMessage(message), objects);
    }

    public static void warn(String message, Object obj) {
        create().warn(buildMessage(message), obj);
    }

    public static void warn(String message, Throwable throwable) {
        create().warn(buildMessage(message), throwable);
    }

    public static void error(String message) {
        create().error(buildMessage(message));
    }

    public static void error(String message, Object... objects) {
        create().error(buildMessage(message), objects);
    }

    public static void error(String message, Object obj) {
        create().error(buildMessage(message), obj);
    }

    public static void error(String message, Throwable throwable) {
        create().error(buildMessage(message), throwable);
    }

}
