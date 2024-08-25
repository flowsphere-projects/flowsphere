package com.flowsphere.common.propagator;

import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;

public interface GatewayPropagator {

    void inject(InstantWeight instantWeight, HeaderResolver headerResolver);

}
