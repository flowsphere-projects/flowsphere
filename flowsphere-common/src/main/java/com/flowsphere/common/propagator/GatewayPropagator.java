package com.flowsphere.common.propagator;

import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.request.AbstractAttributeResolver;

public interface GatewayPropagator {

    void inject(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

}
