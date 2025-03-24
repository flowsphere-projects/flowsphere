package com.thalossphere.agent.plugin.spring.cloud.gateway.propagator;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.propagator.AbstractGatewayPropagator;
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
