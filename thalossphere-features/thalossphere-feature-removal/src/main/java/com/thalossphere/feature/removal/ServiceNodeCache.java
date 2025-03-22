package com.thalossphere.feature.removal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceNodeCache {

    private static final Map<String, ServiceNode> INSTANCE_CALL_RESULT = new ConcurrentHashMap<>();

    public static void saveInstanceCallResult(String key, ServiceNode instantInfo) {
        INSTANCE_CALL_RESULT.put(key, instantInfo);

    }

    public static Map<String, ServiceNode> getInstanceCallResult() {
        return INSTANCE_CALL_RESULT;
    }

    public static ServiceNode getInstance(String key) {
        return INSTANCE_CALL_RESULT.get(key);
    }

}
