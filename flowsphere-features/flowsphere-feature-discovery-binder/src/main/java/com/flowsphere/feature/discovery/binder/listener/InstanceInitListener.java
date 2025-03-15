package com.flowsphere.feature.discovery.binder.listener;

import com.flowsphere.common.longpoll.LongPollService;
import com.flowsphere.common.utils.NetUtils;
import com.flowsphere.feature.discovery.binder.ConsumerInterfaceUrlManager;
import com.flowsphere.feature.discovery.binder.InstanceService;
import com.flowsphere.feature.discovery.binder.ProviderInterfaceManager;
import com.flowsphere.feature.discovery.binder.event.InstanceInitEvent;
import com.flowsphere.feature.discovery.binder.instance.entity.Consumer;
import com.flowsphere.feature.discovery.binder.instance.entity.Provider;
import com.flowsphere.feature.discovery.binder.instance.entity.ProviderFunction;
import com.google.common.eventbus.Subscribe;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class InstanceInitListener {

    @SneakyThrows
    @Subscribe
    public void listener(InstanceInitEvent instanceInitEvent) {
        LongPollService.getInstance().startLongPolling(instanceInitEvent.getServerAddr(),
                instanceInitEvent.getApplicationName(), NetUtils.getIpAddress(), instanceInitEvent.getPort());
        register(instanceInitEvent);
    }


    @SneakyThrows
    private void register(InstanceInitEvent instanceInitEvent) {
        InstanceService.registerProviderInstance(new Provider()
                .setProviderName(instanceInitEvent.getApplicationName())
                .setIp(NetUtils.getIpAddress())
                .setPort(instanceInitEvent.getPort()));
        List<ProviderFunction> providerFunctionList = buildProviderFunctionList(instanceInitEvent,
                ProviderInterfaceManager.getInterfaceUrlList());
        InstanceService.registerProviderFunction(providerFunctionList);
        Map<String, List<String>> consumerInterfaceList = ConsumerInterfaceUrlManager.getInterfaceUrlList();
        InstanceService.reportConsumerInterface(new Consumer()
                .setApplicationName(instanceInitEvent.getApplicationName())
                .setDependOnInterfaceList(consumerInterfaceList));
    }


    private List<ProviderFunction> buildProviderFunctionList(InstanceInitEvent instanceInitEvent, List<String> providerInterfaceUrlList) {
        List<ProviderFunction> providerFunctionList = new ArrayList<>();
        for (String url : providerInterfaceUrlList) {
            ProviderFunction providerFunction = new ProviderFunction();
            providerFunction.setUrl(url);
            providerFunction.setIp(instanceInitEvent.getServerAddr());
            providerFunction.setProviderName(instanceInitEvent.getApplicationName());
            providerFunctionList.add(providerFunction);
        }
        return providerFunctionList;
    }


}
