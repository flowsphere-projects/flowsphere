package com.flowsphere.agent.core.config;

import com.flowsphere.agent.core.config.yaml.YamlClassPointcutConfig;
import com.flowsphere.agent.core.config.yaml.YamlPointcutConfig;
import com.flowsphere.agent.core.yaml.YamlResolver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PointcutConfigLoader {

    public static List<YamlClassPointcutConfig> load(String pluginName, ClassLoader classLoader) {
        return Optional.ofNullable(YamlResolver.parsePointcutConfig(pluginName, classLoader))
                .map(YamlPointcutConfig::getPointcutConfigs)
                .orElse(new ArrayList<>());
    }

}
