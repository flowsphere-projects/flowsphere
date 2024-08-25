package com.flowsphere.test.plugin.config;

import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.support.local.LocalPluginConfigLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class LocalPluginConfigLoaderTest {

    @Test
    public void loadTest() {
        LocalPluginConfigLoader localPluginConfigLoader = new LocalPluginConfigLoader();
        PluginConfig pluginConfig = localPluginConfigLoader.load(LocalPluginConfigLoader.class.getClassLoader(), null);
        Assertions.assertTrue(Objects.nonNull(pluginConfig));
    }

}
