package com.flowsphere.common.instance;

import com.flowsphere.common.instance.entity.Consumer;
import com.flowsphere.common.instance.entity.Provider;
import com.flowsphere.common.instance.entity.ProviderFunction;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;

import java.util.List;

public class InstanceService {

    public static void reportConsumerInterface(String serverAddr, Consumer consumer) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                .setUrl(serverAddr + "/consumer/save")
                .setData(consumer));
    }

    public static void registerProvider(String serverAddr, Provider provider) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                .setUrl(serverAddr + "/provider/registerInstant")
                .setData(provider));
    }

    public static void registerProviderFunction(String serverAddr, List<ProviderFunction> providerFunctionList) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                .setUrl(serverAddr + "/provider/registerInstantFunction")
                .setData(providerFunctionList));
    }



}
