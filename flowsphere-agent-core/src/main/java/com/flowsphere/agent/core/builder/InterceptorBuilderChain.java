package com.flowsphere.agent.core.builder;

import net.bytebuddy.dynamic.DynamicType;

public class InterceptorBuilderChain {

    public static DynamicType.Builder<?> buildInterceptor(DynamicType.Builder<?> builder, InterceptorBuilder... interceptorBuilders) {
        DynamicType.Builder<?> result = builder;
        for (InterceptorBuilder interceptorBuilder : interceptorBuilders) {
            result = interceptorBuilder.intercept(result);
        }
        return result;
    }

}
