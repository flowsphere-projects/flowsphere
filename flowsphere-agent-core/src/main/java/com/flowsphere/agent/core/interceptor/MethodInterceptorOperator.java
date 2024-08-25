package com.flowsphere.agent.core.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;

public interface MethodInterceptorOperator {

    DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder, MethodDescription pointcut);

}
