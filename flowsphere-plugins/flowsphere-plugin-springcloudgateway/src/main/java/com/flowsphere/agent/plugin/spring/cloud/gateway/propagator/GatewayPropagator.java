package com.flowsphere.agent.plugin.spring.cloud.gateway.propagator;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.propagator.AbstractGatewayPropagator;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class GatewayPropagator extends AbstractGatewayPropagator {

    private final ServerHttpRequest request;


    public GatewayPropagator(ServerHttpRequest request) {
        this.request = request;
    }


    @Override
    public void doInject(String tag) {
        ServerHttpRequest.Builder requsetBuilder = request.mutate();
        requsetBuilder.headers(headers -> headers.add(CommonConstant.TAG, tag));
    }

}
