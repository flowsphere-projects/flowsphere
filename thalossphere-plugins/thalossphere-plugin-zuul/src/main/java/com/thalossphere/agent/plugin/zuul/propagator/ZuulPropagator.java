package com.thalossphere.agent.plugin.zuul.propagator;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.propagator.AbstractGatewayPropagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;

@Slf4j
public class ZuulPropagator extends AbstractGatewayPropagator {

    private final RibbonCommandContext commandContext;

    public ZuulPropagator(RibbonCommandContext commandContext) {
        this.commandContext = commandContext;
    }

    @Override
    public void doInject(String tag) {
        if (log.isDebugEnabled()) {
            log.debug("[thalossphere] zuul doInject tag={}", tag);
        }
        commandContext.getHeaders().add(CommonConstant.TAG, tag);
    }

}
