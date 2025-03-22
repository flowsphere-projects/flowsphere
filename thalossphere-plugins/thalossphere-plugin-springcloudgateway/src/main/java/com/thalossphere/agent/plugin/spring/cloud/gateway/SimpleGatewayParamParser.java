package com.thalossphere.agent.plugin.spring.cloud.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.param.GatewayParamParser;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.sc.ServerWebExchangeItemParser;
import com.alibaba.csp.sentinel.util.function.Predicate;
import org.springframework.web.server.ServerWebExchange;

public class SimpleGatewayParamParser {

    public static Object[] parseParameterFor(String resource, ServerWebExchange request, Predicate<GatewayFlowRule> rulePredicate) {
        return new GatewayParamParser<>(new ServerWebExchangeItemParser()).parseParameterFor(resource, request, rulePredicate);
    }

}
