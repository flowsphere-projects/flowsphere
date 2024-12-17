package com.flowsphere.feature.discovery.binder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderInterfaceManager {

    private static final List<String> INTERFACE = new ArrayList<>();

    public static void addInterfaceUrl(String url) {
        INTERFACE.add(url);
    }

    public static List<String> getInterfaceUrlList() {
        return INTERFACE;
    }

    public static void clear() {
        INTERFACE.clear();
    }

}
