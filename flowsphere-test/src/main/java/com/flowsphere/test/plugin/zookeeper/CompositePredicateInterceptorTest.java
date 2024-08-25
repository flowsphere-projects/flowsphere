package com.flowsphere.test.plugin.zookeeper;

import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.plugin.zookeeper.CompositePredicateInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.tag.context.TagContext;
import com.netflix.loadbalancer.Server;
import lombok.SneakyThrows;
import org.apache.curator.x.discovery.ServiceInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.ZookeeperServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositePredicateInterceptorTest {

    @Test
    public void beforeTest() {

        Object[] objects = new Object[]{buildServerList()};
        TagContext.set("TAGA");
        initEnv();
        CompositePredicateInterceptor interceptor = new CompositePredicateInterceptor();
        InstantMethodInterceptorResult instantMethodInterceptorResult = new InstantMethodInterceptorResult();

        interceptor.beforeMethod(null, objects,
                null, null, instantMethodInterceptorResult);
        Assertions.assertTrue(!instantMethodInterceptorResult.isContinue());
        Assertions.assertTrue(((List<Server>) instantMethodInterceptorResult.getResult()).size() == 1);
    }

    private void initEnv() {
        HashMap<String, String> configMap = new HashMap<>();
        configMap.put("spring.cloud.zookeeper.connect-string", "127.0.0.1");
        Env.putAll(configMap);
    }

    @SneakyThrows
    private List<Server> buildServerList() {
        List<Server> serverList = new ArrayList<>();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("tag", "TAGA");
        ZookeeperInstance zookeeperInstance = new ZookeeperInstance("zookeeper","zookeeper",metadata);
        ServiceInstance serviceInstance = ServiceInstance.builder().id("zookeeper")
                .port(2181)
                .address("127.0.0.1")
                .name("zookeeper").payload(zookeeperInstance).enabled(true).build();
        ZookeeperServer zookeeperServer = new ZookeeperServer(serviceInstance);
        serverList.add(zookeeperServer);
        return serverList;
    }

}