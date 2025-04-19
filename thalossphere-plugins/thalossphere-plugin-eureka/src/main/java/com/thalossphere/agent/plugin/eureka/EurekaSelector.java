//package com.thalossphere.agent.plugin.eureka;
//
//import com.netflix.loadbalancer.Server;
//import com.thalossphere.register.center.selector.InstanceSelector;
//import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
//
//import java.util.Map;
//
//public class EurekaSelector extends InstanceSelector {
//
//    private static final EurekaSelector INSTANCE = new EurekaSelector();
//
//    public static EurekaSelector getInstance() {
//        return INSTANCE;
//    }
//
//    //org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.getInstances
//    @Override
//    public Map<String, String> getMetadata(Server server) {
//        if (server instanceof EurekaServiceInstance) {
//            EurekaServiceInstance discoveryEnabledServer = (EurekaServiceInstance) server;
//            return discoveryEnabledServer.getInstanceInfo().getMetadata();
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isValidServer(Server server) {
//        return server instanceof DiscoveryEnabledServer && new EurekaServerPredicate().test((DiscoveryEnabledServer) server);
//    }
//}
