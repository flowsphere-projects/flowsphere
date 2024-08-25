//package com.flowsphere.test.limiter;
//
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
////import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
//import com.flowsphere.agent.plugin.fegin.circuitbreaker.SlowRatioCircuitBreakerLimiter;
//import com.flowsphere.extension.datasource.cache.PluginConfigCache;
//import com.flowsphere.extension.datasource.entity.PluginConfig;
//import com.flowsphere.extension.datasource.entity.SentinelConfig;
//import com.flowsphere.extension.sentinel.limiter.EntryContext;
//import feign.Request;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class SlowRatioCircuitBreakerLimiterTest {
//
//    private static final String KEY = "/myUrl";
//
//    @BeforeEach
//    public void before() {
//        List<DegradeRule> rules = new ArrayList<>();
//        DegradeRule rule = new DegradeRule(KEY)
//                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
//                // Max allowed response time
//                .setCount(0)
//                // Retry timeout (in second)
//                .setTimeWindow(10)
//                // Circuit breaker opens when slow request ratio > 60%
//                .setSlowRatioThreshold(0.01)
//                .setMinRequestAmount(100)
//                .setStatIntervalMs(20000);
//        rules.add(rule);
//
//        DegradeRuleManager.loadRules(rules);
//    }
//
//    @Test
//    public void limitTest() throws InterruptedException {
//        int concurrency = 8;
//
//        CountDownLatch countDownLatch = new CountDownLatch(concurrency);
//
//        AtomicBoolean result = new AtomicBoolean(false);
//        try (MockedStatic<PluginConfigCache> pluginConfigManagerMockedStatic = Mockito.mockStatic(PluginConfigCache.class)) {
//            PluginConfig pluginConfig = new PluginConfig();
//            pluginConfig.setSentinelConfig(new SentinelConfig());
//            pluginConfig.getSentinelConfig().setResourceLimitEnabled(true);
//            pluginConfigManagerMockedStatic.when(() -> PluginConfigCache.get()).thenReturn(pluginConfig);
//            Request request = Mockito.mock(Request.class);
//            Mockito.when(request.url()).thenReturn(KEY);
//            for (int i = 0; i < concurrency; i++) {
//                Thread entryThread = new Thread(() -> {
//                    for (int j = 0; j < 1000; j++) {
//                        try {
//                            SlowRatioCircuitBreakerLimiter.getInstance().limit(request);
//                            sleep(ThreadLocalRandom.current().nextInt(40, 900));
//                        } catch (BlockException e) {
//                            result.set(true);
//                            break;
//                        } finally {
//                            EntryContext.clear();
//                        }
//                    }
//                    countDownLatch.countDown();
//                });
//                entryThread.setName("sentinel-simulate-traffic-task-" + i);
//                entryThread.start();
//            }
//        }
//
//        countDownLatch.await();
//        Assertions.assertTrue(result.get());
//    }
//
//
//    private static void sleep(int timeMs) {
//        try {
//            TimeUnit.MILLISECONDS.sleep(timeMs);
//        } catch (InterruptedException e) {
//            // ignore
//        }
//    }
//}
