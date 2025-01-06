package com.flowsphere.extension.datasource.init;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.loader.PluginConfigLoaderManager;

import java.lang.instrument.Instrumentation;

public class DataSourceInit implements FlowSphereInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        PluginConfig pluginConfig = PluginConfigLoaderManager.getPluginConfigLoader(yamlAgentConfig.getPluginConfigDataSource().getType())
                .load(classLoader, yamlAgentConfig.getPluginConfigDataSource().getPros());
        PluginConfigCache.put(pluginConfig);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
