package com.thalossphere.plugin.mybatis;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.thalossphere.feature.sentinel.limiter.SentinelResource;
import com.thalossphere.plugin.mybatis.flow.MybatisLimiter;
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
