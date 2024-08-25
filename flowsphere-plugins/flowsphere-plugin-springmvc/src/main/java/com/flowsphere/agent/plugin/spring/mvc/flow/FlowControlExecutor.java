package com.flowsphere.agent.plugin.spring.mvc.flow;


import com.flowsphere.extension.sentinel.limiter.SentinelLimiter;
import com.flowsphere.extension.sentinel.limiter.SentinelResource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class FlowControlExecutor {

    private final static List<SentinelLimiter> FLOW_CONTROL_LIST = new ArrayList<>();

    static {
        FLOW_CONTROL_LIST.add(new RateLimiter());
        FLOW_CONTROL_LIST.add(new DynamicMachineIndicatorsLimiter());
    }

    public static void execute(SentinelResource sentinelResource, Callable<?> callable) {
        for (SentinelLimiter abstractSentinelLimiter : FLOW_CONTROL_LIST) {
            abstractSentinelLimiter.limit(sentinelResource, callable);
        }
    }


}
