package com.thalossphere.test.register.center;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.register.center.selector.InstanceSelector;
import com.netflix.loadbalancer.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceSelectorTest {

    @Test
    public void selectInstancesTest() {
        initWarmupConfig();
        InstanceSelector selector = createInstanceSelector();
        List<Server> serverList = selector.selectInstances(buildServerList());
        Assertions.assertTrue(serverList.get(0).getHostPort().equals("127.0.0.1:8080"));
        Assertions.assertTrue(serverList.size() == 1);
    }


    @Test
    public void computeServerWeightTest() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("weight", "30");
        metadata.put("timestamp", (System.currentTimeMillis() - 10 * 60 * 1000) + "");
        Map<String, String> metadata1 = new HashMap<>();
        metadata1.put("weight", "20");
        metadata1.put("timestamp", (System.currentTimeMillis() - 5 * 60 * 1000) + "");
        Map<String, String> metadata2 = new HashMap<>();
        metadata2.put("weight", "10");
        metadata2.put("timestamp", System.currentTimeMillis() + "");
        InstanceSelector selector = createInstanceSelector();
        Assertions.assertTrue(selector.computeServerWeight(metadata) == 30);
        Assertions.assertTrue(selector.computeServerWeight(metadata1) == 10);
        Assertions.assertTrue(selector.computeServerWeight(metadata2) == 1);
    }

    @Test
    public void customWarmupComputeServerWeightTest() throws InterruptedException {
        Map<String, String> metadata2 = new HashMap<>();
        metadata2.put("weight", "10");
        metadata2.put("timestamp", System.currentTimeMillis() + "");
        metadata2.put("warmup", String.valueOf(1000));
        InstanceSelector selector = createInstanceSelector();
        Assertions.assertTrue(selector.computeServerWeight(metadata2) == 1);
        Thread.sleep(1000l);
        Assertions.assertTrue(selector.computeServerWeight(metadata2) == 10);
    }

    private InstanceSelector createInstanceSelector() {
        return new InstanceSelector() {

            @Override
            public Map<String, String> getMetadata(Server server) {
                NacosServer nacosServer = (NacosServer) server;
                return nacosServer.getMetadata();
            }

            @Override
            public boolean isValidServer(Server server) {
                return true;
            }

        };
    }

    private List<Server> buildServerList() {
        List<Server> instanceList = new ArrayList<>();
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("weight", "30");
        metadata.put("timestamp", (System.currentTimeMillis() - 10 * 60 * 1000) + "");
        instance.setMetadata(metadata);
        NacosServer server = new NacosServer(instance);

        Instance instance1 = new Instance();
        instance1.setIp("127.0.0.1");
        instance1.setPort(8081);
        Map<String, String> metadata1 = new HashMap<>();
        metadata1.put("weight", "20");
        metadata1.put("timestamp", (System.currentTimeMillis() - 5 * 60 * 1000) + "");
        instance1.setMetadata(metadata1);
        NacosServer server1 = new NacosServer(instance1);

        Instance instance2 = new Instance();
        instance2.setIp("127.0.0.1");
        instance2.setPort(8082);
        Map<String, String> metadata2 = new HashMap<>();
        metadata2.put("weight", "10");
        metadata2.put("timestamp", System.currentTimeMillis() + "");
        instance2.setMetadata(metadata2);
        NacosServer server2 = new NacosServer(instance2);

        instanceList.add(server);
        instanceList.add(server1);
        instanceList.add(server2);
        return instanceList;
    }

    private void initWarmupConfig() {
        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        yamlAgentConfig.setWarmupEnabled(true);
        YamlAgentConfigCache.put(yamlAgentConfig);
    }


}
