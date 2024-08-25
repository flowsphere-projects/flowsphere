package com.flowsphere.extension.datasource.support.local;


import com.flowsphere.agent.core.utils.AgentPath;
import com.flowsphere.common.yaml.YamlConstructor;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.loader.PluginConfigLoader;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class LocalPluginConfigLoader implements PluginConfigLoader {

    @Override
    public PluginConfig load(ClassLoader classLoader, Properties properties) {
        YamlPluginConfig yamlPluginConfig = parsePluginConfig(classLoader);
        return yamlPluginConfig.getPluginConfig();
    }

    @SneakyThrows
    public YamlPluginConfig parsePluginConfig(ClassLoader classLoader) {
        File path = AgentPath.getPath();
        File file = new File(path, "config/plugin-config.yaml");
        try (InputStream agentYamlInputStream = Files.newInputStream(Paths.get(file.toURI()))) {
            return new Yaml(new YamlConstructor(YamlPluginConfig.class)).loadAs(agentYamlInputStream, YamlPluginConfig.class);
        }
    }

}
