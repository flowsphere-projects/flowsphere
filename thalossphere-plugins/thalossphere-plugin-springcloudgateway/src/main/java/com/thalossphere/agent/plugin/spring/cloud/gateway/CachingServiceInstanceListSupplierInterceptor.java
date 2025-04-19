package com.thalossphere.agent.plugin.spring.cloud.gateway;

import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.tag.context.TagContext;
import com.thalossphere.common.utils.JacksonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class CachingServiceInstanceListSupplierInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Flux<List<ServiceInstance>> serviceInstanceFlux = (Flux<List<ServiceInstance>>) callable.call();
        serviceInstanceFlux.contextWrite(cxt -> cxt.put("TAG", TagContext.get()));
//        Flux<List<ServiceInstance>> result = serviceInstanceFlux
//                .map(serviceList ->
//                        serviceList.stream()
//                                .filter(service -> {
//                                    TagContext.get();
//                                    log.info("获取到service信息："+JacksonUtils.toJson(service));
//                                    String serverTag = service.getMetadata().get(CommonConstant.SERVER_TAG);
//                                    return true;
////                                    return TagContext.get().equals(serverTag);
//                                })
//                                .collect(Collectors.toList())
//                )
//                .doOnError(e -> log.error("Mapping failed", e));
        Flux<List<ServiceInstance>> result = serviceInstanceFlux
                .contextWrite(ctx -> ctx.put("TAG", TagContext.get())) // 确保在操作链中正确写入
                .flatMap(list ->
                        Flux.fromIterable(list)
                                .filterWhen(service ->
                                        Mono.deferContextual(ctx -> {
                                            // 从Reactor Context中获取保存的TAG
                                            String currentTag = ctx.get("TAG");
                                            log.info("获取到service信息：" + JacksonUtils.toJson(service));
                                            String serverTag = service.getMetadata().get(CommonConstant.SERVER_TAG);
                                            // 使用从Context中获取的currentTag进行比较
                                            return Mono.just(currentTag.equals(serverTag));
                                        })
                                )
                                .collectList()
                )
                .doOnError(e -> log.error("Mapping failed", e));

        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(result);
    }

    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        TagContext.remove();
    }
}
