package com.thalossphere.plugin.resttemplate;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequest;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class HttpAccessorInterceptor implements InstanceMethodInterceptor {

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object instantMethodInterceptorResult) {
        ClientHttpRequest request = (ClientHttpRequest) instantMethodInterceptorResult;
        if (log.isDebugEnabled()) {
            log.info("[thalossphere] restTemplate tag={}", TagContext.get());
        }
        request.getHeaders().add(CommonConstant.TAG, TagContext.get());

    }

}
