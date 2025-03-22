package com.thalossphere.extension.datasource.loader;

import com.thalossphere.extension.datasource.entity.PluginConfig;

import java.util.Properties;

public interface PluginConfigLoader {

    PluginConfig load(ClassLoader classLoader, Properties properties);

}
