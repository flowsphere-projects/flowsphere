package com.flowsphere.extension.datasource.support.nacos;

import com.alibaba.nacos.api.config.listener.Listener;
import com.flowsphere.common.concurrent.NamedThreadFactory;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;

import java.util.concurrent.*;

public class NacosConfigListener implements Listener {

    private static final ExecutorService POOL = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new NamedThreadFactory("flowsphere-nacos-ds-update", true), new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    public Executor getExecutor() {
        return POOL;
    }

    @Override
    public void receiveConfigInfo(String config) {
        PluginConfig pluginConfig = JacksonUtils.toObj(config, PluginConfig.class);
        PluginConfigCache.put(pluginConfig);
    }
}
