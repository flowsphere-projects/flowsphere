package com.flowsphere.agent.core.interceptor.template;

import com.flowsphere.agent.core.interceptor.MethodInterceptorOperator;
import com.flowsphere.agent.core.interceptor.type.StaticMethodInterceptor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class StaticMethodInterceptorTemplate implements MethodInterceptorOperator {

    private final Map<String, List<StaticMethodInterceptor>> interceptorMap;

    public StaticMethodInterceptorTemplate(Map<String, List<StaticMethodInterceptor>> interceptorMap) {
        this.interceptorMap = interceptorMap;
    }

    @RuntimeType
    public Object intercept(@Origin final Class<?> klass, @Origin final Method method, @AllArguments final Object[] allArguments, @SuperCall final Callable<?> callable) throws Exception {
        //类似拦截器效果
        Object result = null;
        InstantMethodInterceptorResult instantMethodInterceptorResult = new InstantMethodInterceptorResult();
        try {
            instantMethodInterceptorResult = beforeMethod(klass, method, allArguments, callable, instantMethodInterceptorResult);
            if (!instantMethodInterceptorResult.isContinue()) {
                return instantMethodInterceptorResult.getResult();
            }
            result = callable.call();
            return result;
        } catch (Throwable e) {
            exceptionMethod(klass, method, allArguments, e);
            throw e;
        } finally {
            afterMethod(klass, method, allArguments, result);
        }
    }


    private InstantMethodInterceptorResult beforeMethod(Class<?> klass, Method method, Object[] allArguments, Callable<?> callable, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        for (Map.Entry<String, List<StaticMethodInterceptor>> entry : interceptorMap.entrySet()) {
            for (StaticMethodInterceptor interceptor : entry.getValue()) {
                interceptor.beforeMethod(klass, method, allArguments, callable, instantMethodInterceptorResult);
            }
        }
        return instantMethodInterceptorResult;
    }

    private void afterMethod(Class<?> klass, Method method, Object[] allArguments, Object result) {
        for (Map.Entry<String, List<StaticMethodInterceptor>> entry : interceptorMap.entrySet()) {
            for (StaticMethodInterceptor interceptor : entry.getValue()) {
                interceptor.afterMethod(klass, method, allArguments, result);
            }
        }
    }

    private void exceptionMethod(Class<?> klass, Method method, Object[] allArguments, Throwable throwable) {
        for (Map.Entry<String, List<StaticMethodInterceptor>> entry : interceptorMap.entrySet()) {
            for (StaticMethodInterceptor interceptor : entry.getValue()) {
                interceptor.exceptionMethod(klass, method, allArguments, throwable);
            }
        }
    }


    @Override
    public DynamicType.Builder<?> intercept(DynamicType.Builder<?> builder, MethodDescription pointcut) {
        return builder.method(ElementMatchers.is(pointcut)).intercept(MethodDelegation.withDefaultConfiguration().to(this));
    }

}
