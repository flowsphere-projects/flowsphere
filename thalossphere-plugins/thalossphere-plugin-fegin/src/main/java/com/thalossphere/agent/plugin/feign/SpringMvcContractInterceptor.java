package com.thalossphere.agent.plugin.feign;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.feature.discovery.binder.ConsumerInterfaceUrlManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
public class SpringMvcContractInterceptor implements InstanceMethodInterceptor {

    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Class<?> instant = (Class<?>) allArguments[0];
        FeignClient feignClient = instant.getAnnotation(FeignClient.class);
        Method feignInterfaceMethod = (Method) allArguments[1];
        String[] values = getPathValue(feignInterfaceMethod);
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            ConsumerInterfaceUrlManager.addInterfaceUrl(feignClient.name(), feignClient.path() + value);
        }
    }


    private String[] getPathValue(Method feignInterfaceMethod) {
        PostMapping postMapping = feignInterfaceMethod.getAnnotation(PostMapping.class);
        if (Objects.nonNull(postMapping)) {
            return postMapping.value();
        }
        GetMapping getMapping = feignInterfaceMethod.getAnnotation(GetMapping.class);
        if (Objects.nonNull(getMapping)) {
            return getMapping.value();
        }
        PutMapping putMapping = feignInterfaceMethod.getAnnotation(PutMapping.class);
        if (Objects.nonNull(putMapping)) {
            return putMapping.value();
        }
        DeleteMapping deleteMapping = feignInterfaceMethod.getAnnotation(DeleteMapping.class);
        if (Objects.nonNull(deleteMapping)) {
            return deleteMapping.value();
        }
        RequestMapping requestMapping = feignInterfaceMethod.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            return requestMapping.value();
        }
        return null;
    }

}
