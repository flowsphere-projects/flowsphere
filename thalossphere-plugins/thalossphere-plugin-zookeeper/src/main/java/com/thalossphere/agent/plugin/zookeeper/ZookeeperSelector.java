package com.thalossphere.agent.plugin.zookeeper;

import com.thalossphere.register.center.selector.InstanceSelector;
import com.netflix.loadbalancer.Server;
//import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import java.util.Map;

public class ZookeeperSelector extends InstanceSelector {

    private static final ZookeeperSelector INSTANCE = new ZookeeperSelector();

    public static ZookeeperSelector getInstance() {
        return INSTANCE;
    }

    @Override
    public Map<String, String> getMetadata(Server server) {
//        if (server instanceof ZookeeperServer){
//           ZookeeperServer zookeeperServer = (ZookeeperServer) server;
//           return zookeeperServer.getInstance().getPayload().getMetadata();
//        }
        return null;
    }

    @Override
    public boolean isValidServer(Server server) {
//        return server instanceof ZookeeperServer && new ZookeeperServerPredicate().test((ZookeeperServer) server);
        return false;
    }

}
