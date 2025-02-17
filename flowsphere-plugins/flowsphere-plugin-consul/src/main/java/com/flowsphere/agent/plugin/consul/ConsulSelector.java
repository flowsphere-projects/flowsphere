package com.flowsphere.agent.plugin.consul;

import com.flowsphere.register.center.selector.InstanceSelector;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.consul.discovery.ConsulServer;

import java.util.Map;

public class ConsulSelector extends InstanceSelector {

    private static final ConsulSelector INSTANCE = new ConsulSelector();

    public static ConsulSelector getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, String> getMetadata(Server server) {
        if (server instanceof ConsulServer) {
            ConsulServer consulServer = (ConsulServer) server;
            return consulServer.getMetadata();
        }
        return null;
    }

    @Override
    public boolean isValidServer(Server server) {
        return server instanceof ConsulServer && new ConsulServerPredicate().test((ConsulServer) server);
    }

}
