package com.flowsphere.agent.core.builder;

import com.flowsphere.agent.core.interceptor.MultiThreadConstructorInterceptor;
import com.flowsphere.agent.core.interceptor.MultiThreadMethodInterceptor;
import com.flowsphere.agent.core.matcher.MultiThreadMethodMatch;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class MultiThreadMethodInterceptorBuilder implements InterceptorBuilder {

    @Override
    public DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder) {
        return builder
                .method(new MultiThreadMethodMatch<NamedElement>())
                .intercept(MethodDelegation.withDefaultConfiguration().to(new MultiThreadMethodInterceptor()))
                .constructor(ElementMatchers.isConstructor())
                .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.withDefaultConfiguration().to(new MultiThreadConstructorInterceptor())));
    }

}
