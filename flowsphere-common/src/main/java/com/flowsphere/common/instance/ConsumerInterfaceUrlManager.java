package com.flowsphere.common.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumerInterfaceUrlManager {

    private static final Map<String, List<String>> INTERFACE = new HashMap<>();

    public static void addInterfaceUrl(String serviceName, String url) {
        List<String> urlList = INTERFACE.getOrDefault(serviceName, new ArrayList<>());
        urlList.add(url);
        INTERFACE.put(serviceName, urlList);
    }

    public static Map<String, List<String>> getInterfaceUrlList() {
        return INTERFACE;
    }

    public static void clear() {
        INTERFACE.clear();
    }

}
