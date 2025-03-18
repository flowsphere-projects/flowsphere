package com.flowsphere.register.center.init;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.eventbus.EventBusManager;
import com.flowsphere.common.init.FlowSphereInit;
import com.flowsphere.register.center.listener.RefreshServerListListener;

import java.lang.instrument.Instrumentation;

public class RegisterCenterInit implements FlowSphereInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        EventBusManager.getInstance().register(new RefreshServerListListener());
    }

    @Override
    public int getOrder() {
        return 99;
    }

}
