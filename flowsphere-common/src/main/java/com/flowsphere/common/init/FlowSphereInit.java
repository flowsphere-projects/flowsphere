package com.flowsphere.common.init;

import com.flowsphere.common.config.YamlAgentConfig;

import java.lang.instrument.Instrumentation;

public interface FlowSphereInit {

    void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst);

    int getOrder();

}
