package com.flowsphere.plugin.resttemplate;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequest;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class RestTemplateInterceptor implements InstantMethodInterceptor {

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        ClientHttpRequest request = (ClientHttpRequest) result;
        if (log.isDebugEnabled()) {
            log.info("[flowsphere] RestTemplateInterceptor tag={}", TagContext.get());
        }
        request.getHeaders().add(CommonConstant.TAG, TagContext.get());
    }

}
