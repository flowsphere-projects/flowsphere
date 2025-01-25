package com.flowsphere.feature.discovery.binder;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.config.YamlAgentConfigCache;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;
import com.flowsphere.feature.discovery.binder.instance.entity.Consumer;
import com.flowsphere.feature.discovery.binder.instance.entity.Provider;
import com.flowsphere.feature.discovery.binder.instance.entity.ProviderFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InstanceService {

    public static void reportConsumerInterface(Consumer consumer) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/consumer/save")
                    .setData(consumer));
        } catch (Exception e) {
            log.error("[flowsphere] register consumer fail {}", consumer, e);
        }

    }

    public static void registerProvider(Provider provider) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/registerInstant")
                    .setData(provider));
        } catch (Exception e) {
            log.error("[flowsphere] register provider fail {}", provider, e);
        }

    }

    public static void registerProviderFunction(List<ProviderFunction> providerFunctionList) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/registerInstantFunction")
                    .setData(providerFunctionList));
        } catch (Exception e) {
            log.error("[flowsphere] register provider function fail {}", providerFunctionList, e);
        }
    }

    public static void modifyProviderInstantRemoval(String ip, int status) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("ip", ip);
            param.put("status", status);
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/modifyProviderInstantRemoval")
                    .setData(param));
        } catch (Exception e) {
            log.error("[flowsphere] register provider instant removal fail {}", ip, e);
        }
    }

    private static String getServerAddr() {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        return yamlAgentConfig.getServerAddr();
    }


}
