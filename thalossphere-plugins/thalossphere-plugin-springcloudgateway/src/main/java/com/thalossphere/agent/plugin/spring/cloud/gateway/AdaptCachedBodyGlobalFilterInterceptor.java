package com.thalossphere.agent.plugin.spring.cloud.gateway;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.GatewayApiMatcherManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.matcher.WebExchangeApiMatcher;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.reactor.ContextConfig;
import com.alibaba.csp.sentinel.adapter.reactor.EntryConfig;
import com.alibaba.csp.sentinel.adapter.reactor.SentinelReactorTransformer;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.thalossphere.agent.core.interceptor.type.InstanceMethodInterceptor;
import com.thalossphere.extension.datasource.cache.PluginConfigCache;
import com.thalossphere.extension.datasource.entity.PluginConfig;
import com.thalossphere.extension.datasource.entity.SentinelConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class AdaptCachedBodyGlobalFilterInterceptor implements InstanceMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {

        ServerWebExchange exchange = (ServerWebExchange) allArguments[0];

        if (needLimit(exchange)) {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

            Mono<Void> asyncResult = (Mono<Void>) callable.call();
            if (route != null) {
                String routeId = route.getId();
                Object[] params = SimpleGatewayParamParser.parseParameterFor(routeId, exchange,
                        r -> r.getResourceMode() == SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID);
                String origin = Optional.ofNullable(GatewayCallbackManager.getRequestOriginParser())
                        .map(f -> f.apply(exchange))
                        .orElse("");
                asyncResult = asyncResult.transform(
                        new SentinelReactorTransformer<>(new EntryConfig(routeId, ResourceTypeConstants.COMMON_API_GATEWAY,
                                EntryType.IN, 1, params, new ContextConfig(contextName(routeId), origin)))
                );
            }

            Set<String> matchingApis = pickMatchingApiDefinitions(exchange);
            for (String apiName : matchingApis) {
                Object[] params = SimpleGatewayParamParser.parseParameterFor(apiName, exchange,
                        r -> r.getResourceMode() == SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);
                asyncResult = asyncResult.transform(
                        new SentinelReactorTransformer<>(new EntryConfig(apiName, ResourceTypeConstants.COMMON_API_GATEWAY,
                                EntryType.IN, 1, params))
                );
            }
            instantMethodInterceptorResult.setContinue(false);
            instantMethodInterceptorResult.setResult(asyncResult);
        }

    }


    private String contextName(String route) {
        return SentinelGatewayConstants.GATEWAY_CONTEXT_ROUTE_PREFIX + route;
    }

    private Set<String> pickMatchingApiDefinitions(ServerWebExchange exchange) {
        return GatewayApiMatcherManager.getApiMatcherMap().values()
                .stream()
                .filter(m -> m.test(exchange))
                .map(WebExchangeApiMatcher::getApiName)
                .collect(Collectors.toSet());
    }

    private boolean needLimit(ServerWebExchange exchange) {
        PluginConfig pluginConfig = PluginConfigCache.get();
        if (Objects.isNull(pluginConfig) || Objects.isNull(pluginConfig.getSentinelConfig())) {
            return false;
        }

        SentinelConfig.HttpApiLimitConfig httpApiLimitConfig = pluginConfig.getSentinelConfig().getHttpApiLimitConfig();
        if (Objects.isNull(httpApiLimitConfig)) {
            return false;
        }
        URI uri = exchange.getRequest().getURI();

        return httpApiLimitConfig.isAllUrlLimitEnabled() || httpApiLimitConfig.getExcludeLimitUrlList().stream()
                .anyMatch(url -> url.equals(uri.getPath()));

    }

}
