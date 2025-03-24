package com.thalossphere.common.init;

import com.thalossphere.common.config.YamlAgentConfig;

import java.lang.instrument.Instrumentation;

public interface CoreInit {

    void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst);

    int getOrder();

}
