package com.thalossphere.agent.core.init;

import com.thalossphere.agent.core.AgentBootstrap;
import com.thalossphere.agent.core.AgentJunction;
import com.thalossphere.agent.core.AgentListener;
import com.thalossphere.agent.core.AgentTransform;
import com.thalossphere.agent.core.config.yaml.YamlMethodPointcutConfig;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.init.CoreInit;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.Map;

public class AgentInit implements CoreInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        Map<String, Collection<YamlMethodPointcutConfig>> methodPointcutConfigMap = getMethodPointcutConfig(classLoader, yamlAgentConfig);

        new AgentBuilder.Default()
                .type(new AgentJunction(methodPointcutConfigMap))
                .transform(new AgentTransform(methodPointcutConfigMap, classLoader))
                .with(new AgentListener())
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .installOn(inst);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private static Map<String, Collection<YamlMethodPointcutConfig>> getMethodPointcutConfig(ClassLoader classLoader,
                                                                                             YamlAgentConfig yamlAgentConfig) {
        Map<String, Collection<YamlMethodPointcutConfig>> methodPointcutConfigMap = AgentBootstrap
                .loadPlugins(yamlAgentConfig.getPlugins(), classLoader);
        return methodPointcutConfigMap;
    }

}
