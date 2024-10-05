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
//                .setUrl(getServerAddr() + "/consumer/save")
                .setUrl("http://localhost:8977/consumer/save")
                .setData(consumer));
    }

    public static void registerProvider(String serverAddr, Provider provider) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
//                .setUrl(getServerAddr() + "/provider/registerInstant")
                .setUrl("http://localhost:8977/provider/registerInstant")
                .setData(provider));
    }

    public static void registerProviderFunction(String serverAddr, List<ProviderFunction> providerFunctionList) {
        SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
//                .setUrl(getServerAddr() + "/provider/registerInstantFunction")
                .setUrl("http://localhost:8977/provider/registerInstantFunction")
                .setData(providerFunctionList));
    }



}
