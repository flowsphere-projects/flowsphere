package com.flowsphere.test.removal;

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

}
