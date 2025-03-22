package com.thalossphere.agent;


import com.thalossphere.agent.core.classloader.AgentClassLoader;
import com.thalossphere.agent.core.yaml.YamlResolver;
import com.thalossphere.agent.core.classloader.AgentClassLoaderManager;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.init.CoreInit;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

@Slf4j
public class ThalossphereAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("-------------------thalossphere-agent开始启动-------------------");

        YamlAgentConfig yamlAgentConfig = YamlResolver.parseAgentConfig();

        AgentClassLoader agentClassLoader = AgentClassLoaderManager.getAgentPluginClassLoader(Thread.currentThread().getContextClassLoader());

        init(yamlAgentConfig, agentClassLoader, inst);

        log.info("-------------------thalossphere-agent启动成功-------------------");
    }

    private static void init(YamlAgentConfig yamlAgentConfig, AgentClassLoader agentClassLoader, Instrumentation inst) {
        ServiceLoader<CoreInit> serviceLoader = ServiceLoader.load(CoreInit.class);
        List<CoreInit> FlowSphereInitList = new LinkedList<>();
        for (CoreInit FlowSphereInit : serviceLoader) {
            FlowSphereInitList.add(FlowSphereInit);
        }
        FlowSphereInitList.stream()
                .sorted((o1, o2) -> Integer.compare(o1.getOrder(), o2.getOrder()))
                .forEach(FlowSphereInit -> FlowSphereInit.init(yamlAgentConfig, agentClassLoader, inst));

    }


}
