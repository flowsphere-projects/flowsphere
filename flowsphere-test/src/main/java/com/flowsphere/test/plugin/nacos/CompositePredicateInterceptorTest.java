package com.flowsphere.test.plugin.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.plugin.nacos.CompositePredicateInterceptor;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.tag.context.TagContext;
import com.netflix.loadbalancer.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        configMap.put("spring.cloud.nacos.discovery.server-addr", "127.0.0.1");
        Env.putAll(configMap);
    }

    private List<Server> buildServerList() {
        List<Server> serverList = new ArrayList<>();
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8888);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("tag", "TAGA");
        instance.setMetadata(metadata);
        NacosServer nacosServer = new NacosServer(instance);
        serverList.add(nacosServer);
        return serverList;
    }

}
