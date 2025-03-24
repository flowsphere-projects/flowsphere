package com.thalossphere.agent.core.interceptor.template;

import com.thalossphere.agent.core.interceptor.MethodInterceptorOperator;
import com.thalossphere.agent.core.interceptor.type.ConstructorInterceptor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;
import java.util.Map;

public class ConstructorMethodInterceptorTemplate implements MethodInterceptorOperator {

    private final Map<String, List<ConstructorInterceptor>> interceptorMap;

    public ConstructorMethodInterceptorTemplate(Map<String, List<ConstructorInterceptor>> interceptorMap) {
        this.interceptorMap = interceptorMap;
    }

    @RuntimeType
    public void intercept(
            @This Object obj,
            @AllArguments Object[] allArguments) {
        for (Map.Entry<String, List<ConstructorInterceptor>> entry : interceptorMap.entrySet()) {
            for (ConstructorInterceptor interceptor : entry.getValue()) {
                interceptor.onConstructor(obj, allArguments);
            }
        }
    }

    @Override
    public DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder, MethodDescription pointcut) {
        return builder.constructor(ElementMatchers.is(pointcut))
                .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.withDefaultConfiguration()
                        .to(this)));
    }

}
