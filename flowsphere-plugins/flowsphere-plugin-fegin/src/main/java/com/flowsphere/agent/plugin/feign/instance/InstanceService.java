package com.flowsphere.agent.plugin.feign.instance;

import com.flowsphere.agent.plugin.feign.instance.entity.Consumer;
import com.flowsphere.agent.plugin.feign.instance.entity.Provider;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.FeignConfig;
import com.flowsphere.extension.datasource.entity.PluginConfig;

public class InstanceService {

    public static void reportConsumerInterface(Consumer consumer) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                .setUrl(getServerAddr() + "/consumer/save")
                .setData(consumer));
    }

    public static void registerProvider(Provider provider) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                .setUrl(getServerAddr() + "/provider/registerInstant")
                .setData(provider));
    }


    private static String getServerAddr() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        FeignConfig feignConfig = pluginConfig.getFeignConfig();
        return feignConfig.getServerAddr();
    }

}
