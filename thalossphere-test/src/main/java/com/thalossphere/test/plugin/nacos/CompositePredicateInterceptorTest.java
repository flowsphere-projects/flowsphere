package com.thalossphere.test.plugin.nacos;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.plugin.nacos.CompositePredicateInterceptor;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.env.Env;
import com.thalossphere.common.tag.context.TagContext;
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

        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        yamlAgentConfig.setWarmupEnabled(false);
        YamlAgentConfigCache.put(yamlAgentConfig);
        interceptor.beforeMethod(null, objects,
                null, null, instantMethodInterceptorResult);
        Assertions.assertTrue(!instantMethodInterceptorResult.isContinue());
        Assertions.assertTrue(((List<Server>) instantMethodInterceptorResult.getResult()).size() == 2);
    }


    @Test
    public void warmUpTest() {
        Object[] objects = new Object[]{buildServerList()};
        TagContext.set("TAGA");
        initEnv();
        initWarmupConfig();
        CompositePredicateInterceptor interceptor = new CompositePredicateInterceptor();
        InstantMethodInterceptorResult instantMethodInterceptorResult = new InstantMethodInterceptorResult();

        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        yamlAgentConfig.setWarmupEnabled(true);
        YamlAgentConfigCache.put(yamlAgentConfig);
        interceptor.beforeMethod(null, objects,
                null, null, instantMethodInterceptorResult);
        Assertions.assertTrue(!instantMethodInterceptorResult.isContinue());
        Assertions.assertTrue(((List<Server>) instantMethodInterceptorResult.getResult()).size() == 1);
    }

    private void initWarmupConfig() {
        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        yamlAgentConfig.setWarmupEnabled(true);
        YamlAgentConfigCache.put(yamlAgentConfig);
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
        metadata.put("timestamp", System.currentTimeMillis() + "");
        metadata.put("weight", 100 + "");
        instance.setMetadata(metadata);
        NacosServer nacosServer = new NacosServer(instance);
        serverList.add(nacosServer);
        Instance instance1 = new Instance();
        instance1.setIp("127.0.0.1");
        instance1.setPort(8888);
        Map<String, String> metadata1 = new HashMap<>();
        metadata1.put("tag", "TAGA");
        long tenMinutesInMillis = 10 * 60 * 1000;

        // 10分钟之前的时间戳
        metadata1.put("timestamp", System.currentTimeMillis() - tenMinutesInMillis + "");
        metadata1.put("weight", 100 + "");
        instance1.setMetadata(metadata1);
        NacosServer nacosServer1 = new NacosServer(instance1);
        serverList.add(nacosServer1);
        return serverList;
    }

}
