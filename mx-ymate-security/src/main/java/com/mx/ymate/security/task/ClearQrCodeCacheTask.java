package com.mx.ymate.security.task;

import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import com.mx.ymate.security.adapter.AbstractScanLoginCacheStoreAdapter;
import net.ymate.module.schedule.AbstractScheduleTask;
import net.ymate.module.schedule.ITaskExecutionContext;
import net.ymate.module.schedule.TaskExecutionException;
import net.ymate.module.schedule.annotation.ScheduleTask;
import net.ymate.module.schedule.annotation.TaskConfig;
import net.ymate.platform.log.Logs;


/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
@ScheduleTask(name = "ClearQrCodeCacheTask", description = "0点清空过期的key")
@TaskConfig(name = "ClearQrCodeCacheTask", cron = "0 0 0 * * ?")
public class ClearQrCodeCacheTask extends AbstractScheduleTask {

    @Override
    public void execute(ITaskExecutionContext context) throws TaskExecutionException {
        Logs.get().getLogger().debug("0点清空过期的key");
        ISecurityConfig config = Security.get().getConfig();
        if (!config.openClearExpire()) {
            return;
        }
        AbstractScanLoginCacheStoreAdapter scanLoginCacheStoreAdapter = config.scanLoginCacheStoreAdapter();
        long time = System.currentTimeMillis();
        try {
            scanLoginCacheStoreAdapter.clearCache(config, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}