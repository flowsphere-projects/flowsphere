package com.flowsphere.feature.removal;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.init.FlowSphereInit;

import java.lang.instrument.Instrumentation;

public class RemovalInit implements FlowSphereInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        RemovalTask removalTask = new RemovalTask();
        removalTask.start();
        //TODO 这里需要出则JVM狗子，在应用重启的时候需要关闭线程池
    }

    @Override
    public int getOrder() {
        return 4;
    }

}
