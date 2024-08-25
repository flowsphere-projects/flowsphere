package com.flowsphere.extension.sentinel.datasource;


import com.flowsphere.common.config.PluginConfigDataSource;

public interface SentinelDataSourceRuleLoader {

    void load(PluginConfigDataSource pluginConfigDataSource);

}
