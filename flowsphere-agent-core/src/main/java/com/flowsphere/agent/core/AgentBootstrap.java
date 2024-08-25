package com.flowsphere.agent.core;

import com.flowsphere.agent.core.config.PointcutConfigLoader;
import com.flowsphere.agent.core.config.yaml.YamlClassPointcutConfig;
import com.flowsphere.agent.core.config.yaml.YamlMethodPointcutConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class AgentBootstrap {

    public static Map<String, Collection<YamlMethodPointcutConfig>> loadPlugins(List<String> pluginNameList, ClassLoader classLoader) {

        Map<String, Collection<YamlMethodPointcutConfig>> methodPointcutConfigMap = new HashMap<>();
        for (String pluginName : pluginNameList) {
            List<YamlClassPointcutConfig> classPointcutConfigs = PointcutConfigLoader.load(pluginName, classLoader);
            for (YamlClassPointcutConfig classPointcutConfig : classPointcutConfigs) {
                Collection<YamlMethodPointcutConfig> yamlMethodPointcutConfigs = methodPointcutConfigMap.get(classPointcutConfig.getClassName());
                if (yamlMethodPointcutConfigs == null) {
                    yamlMethodPointcutConfigs = new ArrayList<>();
                }
                yamlMethodPointcutConfigs.addAll(classPointcutConfig.getMethodPointcutConfigs());
                methodPointcutConfigMap.put(classPointcutConfig.getClassName(), yamlMethodPointcutConfigs);
            }
        }
        return methodPointcutConfigMap;
    }


}
