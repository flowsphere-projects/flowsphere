package com.thalossphere.agent.plugin.spring.cloud.gateway;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.common.utils.JacksonUtils;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

public class AbstractErrorWebExceptionHandlerInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        ServerWebExchange exchange = (ServerWebExchange) allArguments[0];
        Throwable throwable = (Throwable) allArguments[1];
        if (throwable instanceof BlockException) {
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(buildErrorResponse(exchange, getLimitErrorResult(), HttpStatus.OK));
        }
    }

    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, String body, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8));
        }));
    }

    private String getLimitErrorResult() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig.getSentinelConfig()) || Objects.isNull(pluginConfig.getSentinelConfig().getLimitReturnResult())) {
            return "{\"message\":\"Too Many Requests\",\"status\":429}";
        }
        Map<String, Object> limitReturnResult = pluginConfig.getSentinelConfig().getLimitReturnResult();
        return JacksonUtils.toJson(limitReturnResult);
    }

}
