package com.flowsphere.agent.plugin.spring.mvc;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import com.flowsphere.extension.datasource.entity.SentinelConfig;
import com.flowsphere.feature.sentinel.utils.SentinelContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class BasicErrorControllerInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        if (Objects.nonNull(SentinelContext.get())) {
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(getLimitReturnResult());
        }
    }

    private ResponseEntity getLimitReturnResult() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        SentinelConfig sentinelConfig = pluginConfig.getSentinelConfig();
        return new ResponseEntity(sentinelConfig.getLimitReturnResult(), HttpStatus.TOO_MANY_REQUESTS);
    }

}
