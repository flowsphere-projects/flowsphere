package com.flowsphere.agent;


import com.flowsphere.agent.core.classloader.AgentClassLoader;
import com.flowsphere.agent.core.yaml.YamlResolver;
import com.flowsphere.agent.core.classloader.AgentClassLoaderManager;
import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

@Slf4j
public class FlowSphereAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("-------------------FlowSphereAgent开始启动-------------------");

        YamlAgentConfig yamlAgentConfig = YamlResolver.parseAgentConfig();

        AgentClassLoader agentClassLoader = AgentClassLoaderManager.getAgentPluginClassLoader(Thread.currentThread().getContextClassLoader());

        init(yamlAgentConfig, agentClassLoader, inst);

        log.info("-------------------FlowSphereAgent启动成功-------------------");
    }

    private static void init(YamlAgentConfig yamlAgentConfig, AgentClassLoader agentClassLoader, Instrumentation inst) {
        ServiceLoader<FlowSphereInit> serviceLoader = ServiceLoader.load(FlowSphereInit.class);
        List<FlowSphereInit> flowSphereInitList = new LinkedList<>();
        for (FlowSphereInit flowSphereInit : serviceLoader) {
            flowSphereInitList.add(flowSphereInit);
        }
        flowSphereInitList.stream()
                .sorted((o1, o2) -> Integer.compare(o1.getOrder(), o2.getOrder()))
                .forEach(flowSphereInit -> flowSphereInit.init(yamlAgentConfig, agentClassLoader, inst));

    }


}
