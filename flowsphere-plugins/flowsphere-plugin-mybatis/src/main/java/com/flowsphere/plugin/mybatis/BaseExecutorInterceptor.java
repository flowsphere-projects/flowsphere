package com.flowsphere.plugin.mybatis;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.features.sentinel.limiter.SentinelResource;
import com.flowsphere.plugin.mybatis.flow.MybatisLimiter;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class BaseExecutorInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        MappedStatement ms = (MappedStatement) allArguments[0];
        Object result = MybatisLimiter.getInstance().limit(new SentinelResource().setResourceName(ms.getId()), callable);
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }


}
