package com.thalossphere.agent.plugin.consul;

import com.netflix.loadbalancer.Server;
import com.thalossphere.register.center.selector.InstanceSelector;

import java.util.Map;

public class ConsulSelector extends InstanceSelector {

    private static final ConsulSelector INSTANCE = new ConsulSelector();

    public static ConsulSelector getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, String> getMetadata(Server server) {
//        if (server instanceof ConsulServer) {
//            ConsulServer consulServer = (ConsulServer) server;
//            return consulServer.getMetadata();
//        }
        return null;
    }

    @Override
    public boolean isValidServer(Server server) {
//        return server instanceof ConsulServer && new ConsulServerPredicate().test((ConsulServer) server);
        return false;
    }

}
