package com.flowsphere.test.async;

import com.flowsphere.common.tag.context.TagContext;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MultiThreadTest {

    private static final String TAG = "1.0.2";

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    @Test
    public void runnableTest() {
        tagRuleReady();
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                asyncTagRuleValidate();
            }
        });
    }

    @Test
    public void nestedRunnableTest() {
        tagRuleReady();
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                asyncTagRuleValidate();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        asyncTagRuleValidate();
                    }
                }).start();
            }
        });
    }

    @Test
    public void runnableMixCallableTest() throws ExecutionException, InterruptedException {
        tagRuleReady();
        Future future = EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                asyncTagRuleValidate();
                EXECUTOR_SERVICE.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        asyncTagRuleValidate();
                        return null;
                    }
                });
            }
        });
        future.get();
    }


    @Test
    public void callableTest() {
        tagRuleReady();
        EXECUTOR_SERVICE.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                asyncTagRuleValidate();
                return null;
            }
        });
    }


    @Test
    public void nestedCallableTest() throws ExecutionException, InterruptedException {
        tagRuleReady();
        Future future = EXECUTOR_SERVICE.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                asyncTagRuleValidate();

                EXECUTOR_SERVICE.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        asyncTagRuleValidate();
                        return null;
                    }
                });
                return null;
            }
        });
        future.get();
    }


    @Test
    public void callableMixRunnableTest() {
        tagRuleReady();
        EXECUTOR_SERVICE.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                asyncTagRuleValidate();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        asyncTagRuleValidate();
                    }
                }).start();

                return null;
            }
        });
    }


    private void tagRuleReady() {
        TagContext.set(TAG);
    }


    private void asyncTagRuleValidate() {
        String tag = TagContext.get();
        assertNotNull(tag);
        assertEquals(tag, TAG);
    }


}