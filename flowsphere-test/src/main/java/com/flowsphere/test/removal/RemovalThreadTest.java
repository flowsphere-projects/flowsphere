package com.flowsphere.test.removal;

import com.flowsphere.feature.removal.RemovalThread;
import com.flowsphere.feature.removal.ServiceNode;
import com.flowsphere.feature.removal.ServiceNodeCache;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

public class RemovalThreadTest {

    @BeforeEach
    public void before() {
        FeignLoadBalancerInterceptorTest test = new FeignLoadBalancerInterceptorTest();
        test.initRemovalConfig();
        test.afterExceptionTest();
    }

    @Test
    public void runTest() {
        RemovalThread removalThread = new RemovalThread();
        removalThread.run();
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        Assertions.assertTrue(Objects.nonNull(instanceCallResult));
        Assertions.assertTrue(CollectionUtils.isNotEmpty(instanceCallResult.values()));
        instanceCallResult.values().forEach(node -> {
            Assertions.assertTrue(node.getErrorRate() >= 1);
        });
    }

}
