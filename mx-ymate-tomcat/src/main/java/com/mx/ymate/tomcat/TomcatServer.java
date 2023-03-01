package com.mx.ymate.tomcat;

import net.ymate.module.embed.IContainer;
import net.ymate.module.embed.Main;

import java.util.ServiceLoader;

/**
 * @Author: xujianpeng.
 * @Date: 2023-02-08 14:36
 * @Description:
 */
public class TomcatServer {

    private static volatile boolean running = true;

    public static void start(String[] args) {
        ServiceLoader<IContainer> containers = ServiceLoader.load(IContainer.class, TomcatServer.class.getClassLoader());
        if (containers.iterator().hasNext()) {
            try {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        MxTomcatContainer.stop();
                        System.out.printf("Container [%s] stopped.%n", MxTomcatContainer.class.getName());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    synchronized (MxTomcatContainer.class) {
                        running = false;
                        MxTomcatContainer.class.notify();
                    }
                }));
                MxTomcatContainer.start(args);
                System.out.printf("Container [%s] started.%n", MxTomcatContainer.class.getName());
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
            synchronized (Main.class) {
                while (running) {
                    try {
                        MxTomcatContainer.class.wait();
                    } catch (Throwable ignored) {
                    }
                }
            }
        } else {
            System.out.println("Warning: No container class was found.");
        }
    }

}
