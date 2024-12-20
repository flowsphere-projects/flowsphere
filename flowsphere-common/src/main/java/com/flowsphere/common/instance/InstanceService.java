package com.flowsphere.common.instance;

import com.flowsphere.common.instance.entity.Consumer;
import com.flowsphere.common.instance.entity.Provider;
import com.flowsphere.common.instance.entity.ProviderFunction;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InstanceService {

    public static void reportConsumerInterface(String serverAddr, Consumer consumer) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(serverAddr + "/consumer/save")
                    .setData(consumer));
        } catch (Exception e) {
            log.error("register consumer fail {} {}", serverAddr, consumer, e);
        }

    }

    public static void registerProvider(String serverAddr, Provider provider) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(serverAddr + "/provider/registerInstant")
                    .setData(provider));
        } catch (Exception e) {
            log.error("register provider fail {} {}", serverAddr, provider, e);
        }

    }

    public static void registerProviderFunction(String serverAddr, List<ProviderFunction> providerFunctionList) {
        try {
            SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                    .setUrl(serverAddr + "/provider/registerInstantFunction")
                    .setData(providerFunctionList));
        } catch (Exception e) {
            log.error("register provider function fail {} {}", serverAddr, providerFunctionList, e);
        }
    }


}
