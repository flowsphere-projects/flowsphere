package com.flowsphere.agent.core.builder;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;

public class TargetObjectInterceptorBuilder implements InterceptorBuilder {

    public static final String CONTEXT_ATTR_NAME = "_$CustomContextAccessorField_ws";

    @Override
    public DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder) {
        return builder.defineField(CONTEXT_ATTR_NAME, Object.class).implement(CustomContextAccessor.class).intercept(FieldAccessor.ofField(CONTEXT_ATTR_NAME));
    }

}
