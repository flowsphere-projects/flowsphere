package com.flowsphere.agent.plugin.zuul.propagator;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.propagator.AbstractGatewayPropagator;
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
            log.debug("[flowsphere] zuul doInject tag={}", tag);
        }
        commandContext.getHeaders().add(CommonConstant.TAG, tag);
    }

}
