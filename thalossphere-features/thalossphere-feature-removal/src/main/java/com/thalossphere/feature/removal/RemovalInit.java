package com.thalossphere.feature.removal;

import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.init.CoreInit;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.RemovalConfig;

import java.lang.instrument.Instrumentation;
import java.util.Objects;

public class RemovalInit implements CoreInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        if (Objects.isNull(removalConfig)) {
            return;
        }
        RemovalTask removalTask = new RemovalTask();
        removalTask.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> removalTask.stop()));
    }

    @Override
    public int getOrder() {
        return 4;
    }

}
