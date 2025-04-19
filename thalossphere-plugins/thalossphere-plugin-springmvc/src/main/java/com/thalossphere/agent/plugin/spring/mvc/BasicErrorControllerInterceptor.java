package com.thalossphere.agent.plugin.spring.mvc;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.SentinelConfig;
import com.thalossphere.feature.sentinel.utils.SentinelContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class BasicErrorControllerInterceptor implements InstanceMethodInterceptor {

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
