package com.flowsphere.test.plugin.config;

import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.support.nacos.NacosPluginConfigLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Properties;

public class NacosPluginConfigLoaderTest {

    @Test
    public void loadTest() {
        NacosPluginConfigLoader nacosPluginConfigLoader = new NacosPluginConfigLoader();
        Properties properties = new Properties();
        properties.setProperty("dataId", "default");
        properties.setProperty("groupId", "DEFAULT_GROUP");
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.setProperty("timeout", "3000");
        PluginConfig pluginConfig = nacosPluginConfigLoader.load(NacosPluginConfigLoader.class.getClassLoader(), properties);
        Assertions.assertTrue(Objects.nonNull(pluginConfig));
    }

}
