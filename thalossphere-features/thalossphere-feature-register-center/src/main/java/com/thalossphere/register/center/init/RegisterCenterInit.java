package com.thalossphere.register.center.init;

import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.eventbus.EventBusManager;
import com.thalossphere.common.init.CoreInit;
import com.thalossphere.register.center.listener.RefreshServerListListener;

import java.lang.instrument.Instrumentation;

public class RegisterCenterInit implements CoreInit {

    @Override
    public void init(YamlAgentConfig yamlAgentConfig, ClassLoader classLoader, Instrumentation inst) {
        EventBusManager.getInstance().register(new RefreshServerListListener());
    }

    @Override
    public int getOrder() {
        return 99;
    }

}
