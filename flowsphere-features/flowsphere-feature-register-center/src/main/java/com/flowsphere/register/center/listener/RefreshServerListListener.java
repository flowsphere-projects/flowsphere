package com.flowsphere.register.center.listener;

import com.flowsphere.common.eventbus.event.RefreshServerListEvent;
import com.flowsphere.common.utils.DynamicServerListLoadBalancerCache;
import com.google.common.eventbus.Subscribe;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class RefreshServerListListener {

    @Subscribe
    public void listener(RefreshServerListEvent refreshServerListEvent) {
        Object result = DynamicServerListLoadBalancerCache.get(refreshServerListEvent.getApplicationName());
        if (Objects.isNull(result)) {
            log.info("[RefreshServerListListener] applicationName={} get dynamicServerListLoadBalancer is null", refreshServerListEvent.getApplicationName());
            return;
        }
        DynamicServerListLoadBalancer dynamicServerListLoadBalancer = (DynamicServerListLoadBalancer) result;
        dynamicServerListLoadBalancer.updateListOfServers();
    }

}
