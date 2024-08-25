package com.flowsphere.extension.datasource.loader;

import com.flowsphere.extension.datasource.entity.PluginConfig;

import java.util.Properties;

public interface PluginConfigLoader {

    PluginConfig load(ClassLoader classLoader, Properties properties);

}
