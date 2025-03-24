package com.thalossphere.test.loadbalance;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.thalossphere.common.loadbalance.ArrayWeightRandom;
import com.thalossphere.common.loadbalance.TagWeight;
import com.thalossphere.register.center.selector.ServerWeight;
import com.netflix.loadbalancer.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayWeightRandomTest {

    private static final String TAG_A = "tagA";

    private static final double TAG_A_WEIGHT = 0.3;

    private static final String TAG_B = "tagB";

    private static final double TAG_B_WEIGHT = 0.7;


    @Test
    public void tagWeightRandomLoadBalanceTest1() {
        AtomicInteger tagACounter = new AtomicInteger();
        AtomicInteger tagA1Counter = new AtomicInteger();
        for (int i = 0; i < 10000; i++) {
            List<TagWeight> tagWeights = new ArrayList<>();
            tagWeights.add(new TagWeight(TAG_A, 1.0));
            tagWeights.add(new TagWeight(TAG_B, 1.0));
            ArrayWeightRandom arrayWeightRandom = new ArrayWeightRandom<TagWeight>(tagWeights);
            String tag = (String) arrayWeightRandom.choose();
            if (tag.equals(TAG_A)) {
                tagACounter.incrementAndGet();
            }
            if (tag.equals(TAG_B)) {
                tagA1Counter.incrementAndGet();
            }
        }
        //随机会有一定误差，取个范围值就好
        Assertions.assertTrue(tagACounter.get() > 2800);
        Assertions.assertTrue(tagA1Counter.get() > 6900);
    }


    @Test
    public void tagWeightRandomLoadBalanceTest() {
        AtomicInteger tagACounter = new AtomicInteger();
        AtomicInteger tagA1Counter = new AtomicInteger();
        for (int i = 0; i < 10000; i++) {
            List<TagWeight> tagWeights = new ArrayList<>();
            tagWeights.add(new TagWeight(TAG_A, TAG_A_WEIGHT));
            tagWeights.add(new TagWeight(TAG_B, TAG_B_WEIGHT));
            ArrayWeightRandom arrayWeightRandom = new ArrayWeightRandom<TagWeight>(tagWeights);
            String tag = (String) arrayWeightRandom.choose();
            if (tag.equals(TAG_A)) {
                tagACounter.incrementAndGet();
            }
            if (tag.equals(TAG_B)) {
                tagA1Counter.incrementAndGet();
            }
        }
        //随机会有一定误差，取个范围值就好
        Assertions.assertTrue(tagACounter.get() > 2800);
        Assertions.assertTrue(tagA1Counter.get() > 6900);
    }


    @Test
    public void serverWeightRandomLoadBalanceTest() {
        AtomicInteger serverACounter = new AtomicInteger();
        AtomicInteger serverA1Counter = new AtomicInteger();
        for (int i = 0; i < 10000; i++) {
            List<ServerWeight> serverWeights = new ArrayList<>();
            serverWeights.add(new ServerWeight(buildServer(), TAG_A_WEIGHT));
            serverWeights.add(new ServerWeight(buildServer1(), TAG_B_WEIGHT));
            ArrayWeightRandom arrayWeightRandom = new ArrayWeightRandom<ServerWeight>(serverWeights);
            Server result = (Server) arrayWeightRandom.choose();
            if (result.getHostPort().equals("127.0.0.1:8080")) {
                serverACounter.incrementAndGet();
            }
            if (result.getHostPort().equals("127.0.0.1:8081")) {
                serverA1Counter.incrementAndGet();
            }
        }
        //随机会有一定误差，取个范围值就好
        Assertions.assertTrue(serverACounter.get() > 2800);
        Assertions.assertTrue(serverA1Counter.get() > 6900);
    }


    private NacosServer buildServer() {
        Instance instance = new Instance();
        instance.setIp("127.0.0.1");
        instance.setPort(8080);
        Map<String, String> metadata = new HashMap<>();
        instance.setMetadata(metadata);
        NacosServer server = new NacosServer(instance);
        return server;
    }

    private NacosServer buildServer1() {
        Instance instance1 = new Instance();
        instance1.setIp("127.0.0.1");
        instance1.setPort(8081);
        Map<String, String> metadata1 = new HashMap<>();
        instance1.setMetadata(metadata1);
        NacosServer server1 = new NacosServer(instance1);
        return server1;
    }

}