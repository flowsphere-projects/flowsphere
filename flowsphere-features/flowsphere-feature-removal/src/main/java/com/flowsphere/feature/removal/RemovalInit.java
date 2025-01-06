package com.flowsphere.feature.removal;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;

import java.lang.instrument.Instrumentation;
import java.util.Objects;

public class RemovalInit implements FlowSphereInit {

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
