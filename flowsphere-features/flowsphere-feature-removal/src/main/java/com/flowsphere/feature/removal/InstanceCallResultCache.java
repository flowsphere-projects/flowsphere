package com.flowsphere.feature.removal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstanceCallResultCache {

    private static final Map<String, Instance> INSTANCE_CALL_RESULT = new ConcurrentHashMap<>();

    public static void saveInstanceCallResult(Instance instantInfo) {
        String key = instantInfo.getHost() + ":" + instantInfo.getPort();
        INSTANCE_CALL_RESULT.put(key, instantInfo);
    }

    public static Map<String, Instance> getInstanceCallResult() {
        return INSTANCE_CALL_RESULT;
    }

    public static Instance getInstance(String key) {
        return INSTANCE_CALL_RESULT.get(key);
    }

}
