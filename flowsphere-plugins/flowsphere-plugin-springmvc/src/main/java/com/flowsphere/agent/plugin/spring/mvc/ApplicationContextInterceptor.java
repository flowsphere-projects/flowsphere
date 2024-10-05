package com.flowsphere.agent.plugin.spring.mvc;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.eventbus.EventBusManager;
import com.flowsphere.common.eventbus.event.InstanceInitEvent;
import com.flowsphere.common.eventbus.listener.InstanceInitListener;
import com.flowsphere.common.utils.StringUtils;
import com.flowsphere.extension.datasource.cache.PluginConfigCache;
import com.flowsphere.extension.datasource.entity.FeignConfig;
import com.flowsphere.extension.datasource.entity.PluginConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ApplicationContextInterceptor implements InstantMethodInterceptor {

    private static final AtomicBoolean STARTER = new AtomicBoolean(false);

    @SneakyThrows
    @Override
    public void afterMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, Object result) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) allArguments[0];
        ConfigurableEnvironment environment = context.getEnvironment();
        String applicationName = environment.getProperty("spring.application.name");
        String serverAddr = getServerAddr();
        if (StringUtils.isNotEmpty(applicationName) && StringUtils.isNotEmpty(serverAddr)) {
            if (STARTER.compareAndSet(false, true)) {
                EventBusManager.getInstance().register(new InstanceInitListener());
                EventBusManager.getInstance().post(new InstanceInitEvent()
                        .setApplicationName(applicationName)
                        .setServerAddr(serverAddr));
            }
        }
    }


    private String getServerAddr() {
        PluginConfig pluginConfig = PluginConfigCache.get();
        FeignConfig feignConfig = pluginConfig.getFeignConfig();
        return Optional.ofNullable(feignConfig).map(FeignConfig::getServerAddr).orElseGet(() -> "");
    }

}
