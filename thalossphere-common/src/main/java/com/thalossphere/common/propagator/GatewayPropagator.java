package com.thalossphere.common.propagator;

import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;

public interface GatewayPropagator {

    void inject(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver);

}
