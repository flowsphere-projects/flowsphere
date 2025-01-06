package com.flowsphere.feature.sentinel.init;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;
import com.flowsphere.extension.datasource.enums.PluginConfigDataSourceTypeEnum;
import com.flowsphere.feature.sentinel.datasource.SentinelDataSourceRuleLoaderManager;

import java.lang.instrument.Instrumentation;

public class SentinelInit implements FlowSphereInit {

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
