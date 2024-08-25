package com.flowsphere.common.env;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Env {

    private final static Map<String, String> ENV = new ConcurrentHashMap<>();

    public static void putAll(Map<String, String> env) {
        ENV.putAll(env);
    }

    public static String get(String key) {
        return ENV.get(key);
    }

}
