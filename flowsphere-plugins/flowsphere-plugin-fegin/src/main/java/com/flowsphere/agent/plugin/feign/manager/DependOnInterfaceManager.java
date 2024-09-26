package com.flowsphere.agent.plugin.feign.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependOnInterfaceManager {

    private static final Map<String, List<String>> DEPEND_ON_INTERFACE = new HashMap<>();

    public static void addInterface(String serviceName, String url) {
        List<String> urlList = DEPEND_ON_INTERFACE.getOrDefault(serviceName, new ArrayList<>());
        urlList.add(url);
        DEPEND_ON_INTERFACE.put(serviceName, urlList);
    }

    public static Map<String, List<String>> getInterfaceList() {
        return DEPEND_ON_INTERFACE;
    }


}
