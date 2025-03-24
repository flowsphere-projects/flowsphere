package com.thalossphere.agent.core.yaml;

import com.thalossphere.agent.core.config.yaml.YamlPointcutConfig;
import com.thalossphere.agent.core.utils.AgentPath;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.yaml.YamlConstructor;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class YamlResolver {

    @SneakyThrows
    public static YamlPointcutConfig parsePointcutConfig(String pluginName, ClassLoader classLoader) {
        try (InputStream agentYamlInputStream = classLoader.getResourceAsStream(String.join(File.separator, pluginName + "-agent.yaml"))) {
            return new Yaml(new YamlConstructor(YamlPointcutConfig.class)).loadAs(agentYamlInputStream, YamlPointcutConfig.class);
        }
    }

    @SneakyThrows
    public static YamlAgentConfig parseAgentConfig() {
        final File path = AgentPath.getPath();
        final File file = new File(path, "config/agent.yaml");
        try (InputStream agentYamlInputStream = Files.newInputStream(Paths.get(file.toURI()))) {
            YamlAgentConfig yamlAgentConfig = new Yaml(new YamlConstructor(YamlAgentConfig.class)).loadAs(agentYamlInputStream, YamlAgentConfig.class);
            YamlAgentConfigCache.put(yamlAgentConfig);
            return yamlAgentConfig;
        }
    }

}
