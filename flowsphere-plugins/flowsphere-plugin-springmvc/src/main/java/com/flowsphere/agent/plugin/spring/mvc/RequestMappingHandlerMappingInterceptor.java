package com.flowsphere.agent.plugin.spring.mvc;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.feature.discovery.binder.ProviderInterfaceManager;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class RequestMappingHandlerMappingInterceptor implements InstantMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        RequestMappingInfo requestMappingInfo = (RequestMappingInfo) allArguments[2];
        for (String url : requestMappingInfo.getPatternsCondition().getPatterns()) {
            ProviderInterfaceManager.addInterfaceUrl(url);
        }
    }

}
