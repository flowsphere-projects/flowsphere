package com.thalossphere.extension.datasource.init;

import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.init.CoreInit;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.loader.PluginConfigLoaderManager;

import java.lang.instrument.Instrumentation;

public class DataSourceInit implements CoreInit {

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
