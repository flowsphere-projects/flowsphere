package com.flowsphere.test.plugin.eureka;

import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.plugin.eureka.BaseLoadBalancerInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.env.Env;
import com.flowsphere.common.tag.context.TagContext;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class BaseLoadBalancerInterceptorTest {


    @Test
    public void beforeTest() {
        initEnv();
        BaseLoadBalancerInterceptor interceptor = new BaseLoadBalancerInterceptor();
        InstantMethodInterceptorResult result = new InstantMethodInterceptorResult();
        TagContext.set("tagA");
        interceptor.beforeMethod(null, null,
                new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return buildDiscoveryEnabledServerList();
                    }
                }, null, result);
        List<DiscoveryEnabledServer> resultList = (List<DiscoveryEnabledServer>) result.getResult();
        Assertions.assertTrue(resultList.size() == 1);
    }


    private void initEnv() {
        HashMap<String, String> configMap = new HashMap<>();
        configMap.put("eureka.client.service-url.defaultZone", "127.0.0.1");
        Env.putAll(configMap);
    }


    private List<DiscoveryEnabledServer> buildDiscoveryEnabledServerList() {
        HashMap<String, String> configMap = new HashMap<>();
        configMap.put("eureka.client.service-url.defaultZone", "127.0.0.1");
        configMap.put(CommonConstant.TAG_KEY, "tagA");
        InstanceInfo instanceInfo = new InstanceInfo("", "", "", "", "", new InstanceInfo.PortWrapper(true, 8080),
                new InstanceInfo.PortWrapper(true, 8080), "", "", "", "", "", "", 1, new DataCenterInfo() {
            @Override
            public Name getName() {
                return Name.Netflix;
            }
        }, "", InstanceInfo.InstanceStatus.DOWN, InstanceInfo.InstanceStatus.DOWN, InstanceInfo.InstanceStatus.DOWN,
                new LeaseInfo(1, 1, 1L, 1L, 1L, 1L, 1L),
                false, configMap, 1L, 1L, InstanceInfo.ActionType.ADDED, "");

        DiscoveryEnabledServer server = new DiscoveryEnabledServer(instanceInfo, false);


        HashMap<String, String> configMap1 = new HashMap<>();
        configMap1.put("eureka.client.service-url.defaultZone", "127.0.0.1");
        configMap1.put(CommonConstant.TAG_KEY, "tagA1");
        InstanceInfo instanceInfo1 = new InstanceInfo("", "", "", "", "", new InstanceInfo.PortWrapper(true, 8080),
                new InstanceInfo.PortWrapper(true, 8080), "", "", "", "", "", "", 1, new DataCenterInfo() {
            @Override
            public Name getName() {
                return Name.Netflix;
            }
        }, "", InstanceInfo.InstanceStatus.DOWN, InstanceInfo.InstanceStatus.DOWN, InstanceInfo.InstanceStatus.DOWN,
                new LeaseInfo(1, 1, 1L, 1L, 1L, 1L, 1L),
                false, configMap1, 1L, 1L, InstanceInfo.ActionType.ADDED, "");

        DiscoveryEnabledServer server1 = new DiscoveryEnabledServer(instanceInfo1, false);

        List<DiscoveryEnabledServer> serverList = new ArrayList<>();
        serverList.add(server);
        serverList.add(server1);
        return serverList;
    }

}
