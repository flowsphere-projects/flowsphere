package com.flowsphere.agent.plugin.zookeeper;

import com.flowsphere.agent.core.context.CustomContextAccessor;
import com.flowsphere.agent.core.interceptor.template.InstantMethodInterceptorResult;
import com.flowsphere.agent.core.interceptor.type.InstantMethodInterceptor;
import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.tag.context.TagManager;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

public class ZookeeperDiscoveryPropertiesInterceptor implements InstantMethodInterceptor {

    @SneakyThrows
    @Override
    public void beforeMethod(CustomContextAccessor customContextAccessor, Object[] allArguments, Callable<?> callable, Method method, InstantMethodInterceptorResult instantMethodInterceptorResult) {
        Map<String, String> metadata = (Map<String, String>) callable.call();
        metadata.put(CommonConstant.TAG_KEY, TagManager.getSystemTag());
        instantMethodInterceptorResult.setContinue(false);
        instantMethodInterceptorResult.setResult(metadata);
    }

}
