package com.thalossphere.agent.plugin.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.thalossphere.register.center.selector.InstanceSelector;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class NacosSelector extends InstanceSelector {

    private static final NacosSelector INSTANCE = new NacosSelector();

    public static NacosSelector getInstance() {
        return INSTANCE;
    }


    @Override
    public Map<String, String> getMetadata(Server server) {
        if (server instanceof NacosServer) {
            NacosServer nacosServer = (NacosServer) server;
            return nacosServer.getMetadata();
        }
        return null;
    }

    @Override
    public boolean isValidServer(Server server) {
        return server instanceof NacosServer && new NacosServerPredicate().test((NacosServer) server);
    }

}
