package com.flowsphere.agent.core.interceptor.type;

import com.flowsphere.agent.core.interceptor.MethodInterceptor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface StaticMethodInterceptor extends MethodInterceptor {

    void beforeMethod(Class<?> clazz, Method method, Object[] args, Callable<?> callable, InstantMethodInterceptorResult instantMethodInterceptorResult);

    void afterMethod(Class<?> clazz, Method method, Object[] args, Object result);

    void exceptionMethod(Class<?> clazz, Method method, Object[] args, Throwable throwable);

}
