package com.flowsphere.agent.plugin.eureka;

import com.flowsphere.register.center.selector.InstanceSelector;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import java.util.Map;

public class EurekaSelector extends InstanceSelector {

    private static final EurekaSelector INSTANCE = new EurekaSelector();

    public static EurekaSelector getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, String> getMetadata(Server server) {
        if (server instanceof DiscoveryEnabledServer) {
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
            return discoveryEnabledServer.getInstanceInfo().getMetadata();
        }
        return null;
    }

    @Override
    public boolean isValidServer(Server server) {
        return server instanceof DiscoveryEnabledServer && new EurekaServerPredicate().test((DiscoveryEnabledServer) server);
    }
}
