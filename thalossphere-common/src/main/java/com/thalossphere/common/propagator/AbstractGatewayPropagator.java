package com.thalossphere.common.propagator;

import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.tag.extract.TagExtractManager;
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
            log.debug("[thalossphere] extract tag={}", tag);
        }
        doInject(tag);
    }

    public abstract void doInject(String tag);

}
