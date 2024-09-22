package com.flowsphere.common.propagator;

import com.flowsphere.common.request.AbstractAttributeResolver;
import com.flowsphere.common.request.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.request.RequestResolver;
import com.flowsphere.common.tag.extract.TagExtractManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractGatewayPropagator implements GatewayPropagator {

    @Override
    public void inject(InstantWeight instantWeight,  AbstractAttributeResolver attributeResolver) {
        if (Objects.isNull(instantWeight)) {
            return;
        }
        String tag = TagExtractManager.extract(instantWeight, attributeResolver);
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] gatewayPropagator extract tag={}", tag);
        }
        doInject(tag);
    }

    public abstract void doInject(String tag);

}
