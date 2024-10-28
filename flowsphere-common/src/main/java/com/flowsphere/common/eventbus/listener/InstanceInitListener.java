package com.flowsphere.common.eventbus.listener;

import com.flowsphere.common.eventbus.event.InstanceInitEvent;
import com.flowsphere.common.instance.ConsumerInterfaceUrlManager;
import com.flowsphere.common.instance.InstanceService;
import com.flowsphere.common.instance.ProviderInterfaceManager;
import com.flowsphere.common.instance.entity.Consumer;
import com.flowsphere.common.instance.entity.Provider;
import com.flowsphere.common.instance.entity.ProviderFunction;
import com.flowsphere.common.longpoll.LongPollService;
import com.flowsphere.common.utils.IpUtils;
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
                instanceInitEvent.getApplicationName(),IpUtils.getIpv4Address());
        register(instanceInitEvent);
    }


    @SneakyThrows
    private void register(InstanceInitEvent instanceInitEvent) {
        Map<String, List<String>> consumerInterfaceList = ConsumerInterfaceUrlManager.getInterfaceUrlList();
        InstanceService.reportConsumerInterface(instanceInitEvent.getServerAddr(), new Consumer()
                .setApplicationName(instanceInitEvent.getApplicationName())
                .setDependOnInterfaceList(consumerInterfaceList));
        InstanceService.registerProvider(instanceInitEvent.getServerAddr(), new Provider()
                .setProviderName(instanceInitEvent.getApplicationName())
                .setIp(IpUtils.getIpv4Address()));
        List<ProviderFunction> providerFunctionList = buildProviderFunctionList(instanceInitEvent,
                ProviderInterfaceManager.getInterfaceUrlList());
        InstanceService.registerProviderFunction(instanceInitEvent.getServerAddr(), providerFunctionList);
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
