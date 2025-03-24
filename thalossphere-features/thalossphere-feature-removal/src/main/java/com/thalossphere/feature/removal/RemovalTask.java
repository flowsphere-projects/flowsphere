package com.thalossphere.feature.removal;

import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RemovalConfig;

import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RemovalTask {

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public void start() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return;
        }
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new RemovalThread(), removalConfig.getWindowsTime(),
                removalConfig.getWindowsTime(), TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
    }


}
