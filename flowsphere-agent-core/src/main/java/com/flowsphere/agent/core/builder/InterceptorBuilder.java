package com.flowsphere.agent.core.builder;

import net.bytebuddy.dynamic.DynamicType;

public interface  InterceptorBuilder {

    DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder);
}
