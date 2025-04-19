package com.thalossphere.agent.plugin.spring.mvc;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.feature.discovery.binder.ProviderInterfaceManager;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class RequestMappingHandlerMappingInterceptor implements InstanceMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        RequestMappingInfo requestMappingInfo = (RequestMappingInfo) allArguments[2];
        for (String url : requestMappingInfo.getPatternsCondition().getPatterns()) {
            ProviderInterfaceManager.addInterfaceUrl(url);
        }
    }

}
