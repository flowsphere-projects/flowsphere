package com.flowsphere.agent.core.init;

import com.flowsphere.agent.core.AgentBootstrap;
import com.flowsphere.agent.core.AgentJunction;
import com.flowsphere.agent.core.AgentListener;
import com.flowsphere.agent.core.AgentTransform;
import com.flowsphere.agent.core.config.yaml.YamlMethodPointcutConfig;
import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.Map;

public class CoreInit implements FlowSphereInit {

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
