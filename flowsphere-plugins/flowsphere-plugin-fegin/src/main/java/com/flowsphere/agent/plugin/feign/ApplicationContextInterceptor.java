package com.flowsphere.agent.plugin.feign;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.agent.plugin.feign.instance.InstanceService;
import com.flowsphere.agent.plugin.feign.instance.entity.Consumer;
import com.flowsphere.agent.plugin.feign.instance.entity.Provider;
import com.flowsphere.agent.plugin.feign.manager.DependOnInterfaceManager;
import com.flowsphere.common.utils.IpUtils;
import com.flowsphere.common.utils.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
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
        if (StringUtils.isNotEmpty(applicationName)) {
            if (STARTER.compareAndSet(false, true)) {
                Map<String, List<String>> interfaceList = DependOnInterfaceManager.getInterfaceList();
                InstanceService.reportConsumerInterface(new Consumer()
                        .setApplicationName(applicationName)
                        .setDependOnInterfaceList(interfaceList));
                InstanceService.registerProvider(new Provider()
                        .setProviderName(applicationName)
                        .setIp(IpUtils.getIpv4Address()));
            }
        }
    }

}
