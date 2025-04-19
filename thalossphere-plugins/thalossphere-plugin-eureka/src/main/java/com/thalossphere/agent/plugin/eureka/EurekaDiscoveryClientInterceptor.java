package com.thalossphere.agent.plugin.eureka;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class EurekaDiscoveryClientInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Object result = callable.call();
        List<EurekaServiceInstance> instanceList = (List<EurekaServiceInstance>) result;
        instanceList = instanceList.stream().filter(instance -> {
            return new EurekaServerPredicate().test(instance);
        }).collect(Collectors.toList());
        instantMethodInterceptorResult.setResult(instanceList);
        instantMethodInterceptorResult.setContinue(false);
    }

}
