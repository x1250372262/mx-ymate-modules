package com.mx.ymate.monitor.helper;

import cn.hutool.core.thread.ThreadUtil;
import com.mx.ymate.dev.support.log.MxLog;
import com.mx.ymate.monitor.IMonitorConfig;
import com.mx.ymate.monitor.bean.project.ProjectBean;
import com.mx.ymate.monitor.mq.RedisMq;

import java.util.concurrent.TimeUnit;

public class ProjectHelper {


    private static boolean runningFlag = false;


    public static void start(IMonitorConfig config) {
        runningFlag = true;
        ThreadUtil.execAsync(() -> {
            while (runningFlag) {
                ProjectBean projectBean = ProjectBean.onlineBean(config);
                MxLog.info("发布项目信息");
                RedisMq.pushProject(projectBean);
                ThreadUtil.sleep(config.time(), TimeUnit.SECONDS);
            }
            return true;
        });
    }

    public static void stop() {
        runningFlag = false;
    }


}
