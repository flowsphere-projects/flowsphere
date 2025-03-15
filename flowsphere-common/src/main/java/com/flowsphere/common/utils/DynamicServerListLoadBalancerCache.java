package com.flowsphere.common.utils;

import java.util.HashMap;
import java.util.Map;

public class DynamicServerListLoadBalancerCache {

    private static final Map<String, Object> CACHE = new HashMap<>();

    public static void add(String clientName, Object dynamicServerListLoadBalancer) {
        CACHE.put(clientName, dynamicServerListLoadBalancer);
    }

    public static Object get(String clientName) {
        return CACHE.get(clientName);
    }

}
