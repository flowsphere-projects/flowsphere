package com.thalossphere.feature.sentinel.init;

import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.init.CoreInit;
import com.thalossphere.extension.datasource.enums.PluginConfigDataSourceTypeEnum;
import com.thalossphere.feature.sentinel.datasource.SentinelDataSourceRuleLoaderManager;

import java.lang.instrument.Instrumentation;

public class SentinelInit implements CoreInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        if (!yamlAgentConfig.isSentinelEnabled() || PluginConfigDataSourceTypeEnum.LOCAL.getType()
                .equals(yamlAgentConfig.getPluginConfigDataSource().getType())) {
            return;
        }
        SentinelDataSourceRuleLoaderManager.getLoader(yamlAgentConfig.getPluginConfigDataSource().getType())
                .load(yamlAgentConfig.getPluginConfigDataSource());
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
