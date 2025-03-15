package com.flowsphere.feature.discovery.binder;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.config.YamlAgentConfigCache;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;
import com.flowsphere.feature.discovery.binder.instance.entity.Consumer;
import com.flowsphere.feature.discovery.binder.instance.entity.Provider;
import com.flowsphere.feature.discovery.binder.instance.entity.ProviderFunction;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InstanceService {

    public static void reportConsumerInterface(Consumer consumer) {
        try {
            responseHandler(SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/consumer/save")
                    .setData(consumer)));
        } catch (Exception e) {
            log.error("[flowsphere] register consumer fail {}", consumer, e);
        }

    }

    public static void registerProviderInstance(Provider provider) {
        try {
            responseHandler(SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/registerInstance")
                    .setData(provider)));
        } catch (Exception e) {
            log.error("[flowsphere] register provider fail {}", provider, e);
        }

    }

    public static void registerProviderFunction(List<ProviderFunction> providerFunctionList) {
        try {
            responseHandler(SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/registerInstanceFunction")
                    .setData(providerFunctionList)));
        } catch (Exception e) {
            log.error("[flowsphere] register provider function fail {}", providerFunctionList, e);
        }
    }

    public static void modifyProviderInstanceRemoval(String ip, int port, int status) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("ip", ip);
            param.put("port",port);
            param.put("status", status);
            responseHandler(SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(getServerAddr() + "/provider/modifyProviderInstanceRemoval")
                    .setData(param)));
        } catch (Exception e) {
            log.error("[flowsphere] register provider instant removal fail {}", ip, e);
        }
    }



    private static String getServerAddr() {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        return yamlAgentConfig.getServerAddr();
    }

    public static void responseHandler(Response response) {
        if (!response.isSuccessful()) {
            throw new RuntimeException("okhttp post execute error");
        }
    }

}
