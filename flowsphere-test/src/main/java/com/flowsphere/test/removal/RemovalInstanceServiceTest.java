package com.flowsphere.test.removal;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.flowsphere.common.config.OkHttpConfig;
import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.config.YamlAgentConfigCache;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.RemovalConfig;
import com.flowsphere.feature.removal.RemovalInstanceService;
import com.flowsphere.feature.removal.ServiceNode;
import com.flowsphere.feature.removal.ServiceNodeCache;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemovalInstanceServiceTest {

    @BeforeEach
    public void before() {
        initServiceNode();
        FeignLoadBalancerInterceptorTest test = new FeignLoadBalancerInterceptorTest();
        test.initRemovalConfig();
        test.afterExceptionTest();
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        instanceCallResult.values().forEach(node -> {
            node.setHost("127.0.0.1");
            node.setPort(8080);
            node.setErrorRate(2);
        });
        YamlAgentConfig yamlAgentConfig = new YamlAgentConfig();
        OkHttpConfig okHttpConfig = new OkHttpConfig();
        okHttpConfig.setConnectTimeout(30L);
        okHttpConfig.setWriteTimeout(30L);
        okHttpConfig.setReadTimeout(30L);
        yamlAgentConfig.setOkHttpConfig(okHttpConfig);
        yamlAgentConfig.setServerAddr("http://localhost:8224");
        YamlAgentConfigCache.put(yamlAgentConfig);

    }

    private void initServiceNode() {
        String key = "127.0.0.1:8081";
        ServiceNode tmpServiceNode = new ServiceNode();
        tmpServiceNode.setLastInvokeTime(System.currentTimeMillis());
        tmpServiceNode.getRequestFailNum().incrementAndGet();
        tmpServiceNode.setHost("127.0.0.1");
        tmpServiceNode.setPort(8081);
        tmpServiceNode.getRequestNum().incrementAndGet();
        ServiceNodeCache.saveInstanceCallResult(key, tmpServiceNode);

        String key1 = "127.0.0.1:8082";
        ServiceNode tmpServiceNode1 = new ServiceNode();
        tmpServiceNode1.getRequestFailNum().incrementAndGet();
        tmpServiceNode1.setLastInvokeTime(System.currentTimeMillis());
        tmpServiceNode1.setHost("127.0.0.1");
        tmpServiceNode1.setPort(8082);
        tmpServiceNode1.getRequestNum().incrementAndGet();
        ServiceNodeCache.saveInstanceCallResult(key1, tmpServiceNode1);
    }

    @Test
    public void removalPartInstanceTest() {
        RemovalInstanceService removalInstanceService = RemovalInstanceService.getInstance();
        List<Server> serverList = getServerList();
        removalInstanceService.removal(serverList);
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(instanceCallResult.get("127.0.0.1:8080").getRemovalStatus().get());
        List<ServiceNode> normalServiceNodeList = instanceCallResult.values().stream().filter(node -> !node.getRemovalStatus().get()).collect(Collectors.toList());
        Assertions.assertTrue(normalServiceNodeList.size() == 1);
        List<ServiceNode> removalServiceNodeList = instanceCallResult.values().stream().filter(node -> node.getRemovalStatus().get()).collect(Collectors.toList());
        Assertions.assertTrue(removalServiceNodeList.size() == 2);
    }

    @Test
    public void repeatRemovalPartInstanceTest() {
        RemovalInstanceService removalInstanceService = RemovalInstanceService.getInstance();
        List<Server> serverList = getServerList();
        removalInstanceService.removal(serverList);
        removalInstanceService.removal(serverList);
        removalInstanceService.removal(serverList);
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(instanceCallResult.get("127.0.0.1:8080").getRemovalStatus().get());
        List<ServiceNode> normalServiceNodeList = instanceCallResult.values().stream().filter(node -> !node.getRemovalStatus().get()).collect(Collectors.toList());
        Assertions.assertTrue(normalServiceNodeList.size() == 1);
        List<ServiceNode> removalServiceNodeList = instanceCallResult.values().stream().filter(node -> node.getRemovalStatus().get()).collect(Collectors.toList());
        Assertions.assertTrue(removalServiceNodeList.size() == 2);
    }

    @Test
    public void removalAllInstanceTest() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        RemovalConfig removalConfig = pluginConfig.getRemovalConfig();
        removalConfig.setMinInstanceNum(0);
        RemovalInstanceService removalInstanceService = RemovalInstanceService.getInstance();
        removalInstanceService.removal(getServerList());
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(instanceCallResult.get("127.0.0.1:8080").getRemovalStatus().get());
        List<ServiceNode> normalServiceNodeList = instanceCallResult.values().stream().filter(node -> node.getRemovalStatus().get()).collect(Collectors.toList());
        Assertions.assertTrue(normalServiceNodeList.size() == 3);
    }

    @Test
    public void getCanRemovalNumTest() {
        RemovalConfig removalConfig = new RemovalConfig();
        removalConfig.setApplicationName("service-b");
        removalConfig.setMinInstanceNum(1);
        removalConfig.setErrorRate(1.0D);
        removalConfig.setRecoveryTime(10000L);
        removalConfig.setScaleUpLimit(1.0D);
        removalConfig.setWindowsTime(10);
        removalConfig.setExceptions(Lists.newArrayList("java.lang.RuntimeException"));
        RemovalInstanceService removalInstanceService = RemovalInstanceService.getInstance();
        List<Server> instanceList = new ArrayList<>();
        Server server = new Server("127.0.0.1", 8080);
        Server server1 = new Server("127.0.0.1", 8081);
        instanceList.add(server);
        instanceList.add(server1);
        List<Server> removalInstanceList = new ArrayList<>();
        removalInstanceList.add(server);
        removalInstanceService.getCanRemovalNum(instanceList, removalInstanceList, removalConfig);
    }

    private List<Server> getServerList() {
        List<Server> instanceList = new ArrayList<>();
        Server server = new Server("127.0.0.1", 8080);
        Server server1 = new Server("127.0.0.1", 8081);
        Server server2 = new Server("127.0.0.1", 8082);
        instanceList.add(server);
        instanceList.add(server1);
        instanceList.add(server2);
        return instanceList;
    }

    @Test
    public void warmUpTest() {
        List<NacosServer> instanceList = new ArrayList<>();
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("weight", "10");
        metadata.put("timestamp", System.currentTimeMillis() - 300000000 + "");
        instance.setMetadata(metadata);
        NacosServer server = new NacosServer(instance);

        Instance instance1 = new Instance();
        instance1.setIp("127.0.0.1");
        instance1.setPort(8081);
        Map<String, String> metadata1 = new HashMap<>();
        metadata1.put("weight", "10");
        metadata1.put("timestamp", System.currentTimeMillis() - 20000000 + "");
        instance1.setMetadata(metadata1);
        NacosServer server1 = new NacosServer(instance1);

        Instance instance2 = new Instance();
        instance2.setIp("127.0.0.1");
        instance2.setPort(8082);
        Map<String, String> metadata2 = new HashMap<>();
        metadata2.put("weight", "10");
        metadata2.put("timestamp", System.currentTimeMillis() - 10000000 + "");
        instance2.setMetadata(metadata2);
        NacosServer server2 = new NacosServer(instance2);

        instanceList.add(server);
        instanceList.add(server1);
        instanceList.add(server2);
        for (NacosServer nacosServer : instanceList) {
            System.out.println(getWeight(nacosServer));
        }
    }


    int getWeight(NacosServer server) {
        //比例权重
        int weight = Integer.parseInt(server.getMetadata().get("weight"));
        if (weight > 0) {
            //上线时间
            long timestamp = Long.parseLong(server.getMetadata().get("timestamp"));
            if (timestamp > 0L) {
                long uptime = System.currentTimeMillis() - timestamp;
                if (uptime < 0) {
                    return 1;
                }
                //warmup时长
//                int warmup = Integer.parseInt(server.getMetadata().get("warmup"));
                int warmup = 10 * 60 * 1000;
                if (uptime < warmup) {
                    weight = calculateWarmupWeight((int) uptime, warmup, weight);
                }
            }
        }
        return Math.max(weight, 0);
    }

    static int calculateWarmupWeight(int uptime, int warmup, int weight) {
        int ww = (int) (uptime / ((float) warmup / weight));
        System.out.println(uptime + "=" + warmup + "=" + weight + "=" + ww);
        return ww < 1 ? 1 : (Math.min(ww, weight));
    }


}
