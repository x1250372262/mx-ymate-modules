package com.mx.ymate.security.task;

import cn.hutool.core.util.StrUtil;
import com.mx.ymate.redis.api.RedisApi;
import com.mx.ymate.security.ISecurityConfig;
import com.mx.ymate.security.Security;
import net.ymate.module.schedule.AbstractScheduleTask;
import net.ymate.module.schedule.ITaskExecutionContext;
import net.ymate.module.schedule.TaskExecutionException;
import net.ymate.module.schedule.annotation.ScheduleTask;
import net.ymate.module.schedule.annotation.TaskConfig;
import net.ymate.platform.log.Logs;
import net.ymate.platform.persistence.redis.Redis;
import redis.clients.jedis.ScanParams;

import java.util.List;


/**
 * @Author: MaHaoYu.
 * @Create: 2023/12/12 9:12
 * @Description:
 */
@ScheduleTask(name = "ClearQrCodeCacheTask", description = "0点清空过期的key")
@TaskConfig(name = "ClearQrCodeCacheTask", cron = "0 0 0 * * ?")
public class ClearQrCodeCacheTask extends AbstractScheduleTask {

    private static final String KEY_PATTERN = "{}:qrcodeKey:*";

    @Override
    public void execute(ITaskExecutionContext context) throws TaskExecutionException {
        Logs.get().getLogger().debug("0点清空过期的key");
        ISecurityConfig config = Security.get().getConfig();
        if (!config.openClearExpire()) {
            return;
        }
        long time = System.currentTimeMillis();
        try {
            String key = StrUtil.format(KEY_PATTERN, config.project());
            ScanParams scanParams = new ScanParams().match(key).count(1000);
            List<String> scanResultList = Redis.get().openSession(session -> session.getConnectionHolder().getConnection().scan("0", scanParams)).getResult();
            for (String scanResult : scanResultList) {
                Long ttl = RedisApi.getExpire(scanResult);
                Logs.get().getLogger().debug("ttl::::" + ttl);
                // 检查是否已过期
                if (ttl != null && ttl < time) {
                    RedisApi.strDelete(scanResult);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}