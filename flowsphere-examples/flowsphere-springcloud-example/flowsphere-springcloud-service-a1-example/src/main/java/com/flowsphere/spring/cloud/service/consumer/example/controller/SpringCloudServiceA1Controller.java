package com.flowsphere.spring.cloud.service.consumer.example.controller;

import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.spring.cloud.service.api.SpringCloudBApi;
import com.flowsphere.spring.cloud.service.api.result.TagResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping()
public class SpringCloudServiceA1Controller {

    @Autowired
    private SpringCloudBApi springCloudBApi;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @SneakyThrows
    @GetMapping("/lazy")
    public String lazy() {
        Thread.sleep(300);
        return "lazy";
    }

    @PostMapping("/helloWord")
    public List<TagResult> helloWord(String str) {
        List<TagResult> tagResults = springCloudBApi.helloWord(str);
        tagResults.add(TagResult.build("SpringCloudProviderA1"));
        return tagResults;
    }

    @PostMapping("/repeat")
    public List<TagResult> repeat(String str) {
        springCloudBApi.helloWord(str);
        List<TagResult> tagResults = springCloudBApi.repeat(str);
        tagResults.add(TagResult.build("SpringCloudProviderA1"));
        return tagResults;
    }

    @PostMapping("/helloWordAsync")
    public String helloWordAsync(String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("获取到tag=" + TagContext.get());
                springCloudBApi.helloWord(str);
            }
        }).start();

        return "[App=FlowSphereSpringCloudProviderA1 Tag=" + TagManager.getTag() + "] -> " + springCloudBApi.repeat(str);
    }


    /**
     * runnableLamabdaThread
     *
     * @param str
     * @return
     */
    @PostMapping("/runnableLambdaThread")
    public String runnableLambdaThread(final String str) {
        new Thread(() -> {
            System.out.println("获取到tag=" + TagContext.get());
            springCloudBApi.helloWord(str);
        }).start();
        return "runnableLambdaThread";
    }


    /**
     * SingleThread
     *
     * @param str
     * @return
     */
    @PostMapping("/runnableThread")
    public String runnableThreadHelloWord(final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("获取到tag=" + TagContext.get());
                springCloudBApi.helloWord(str);
            }
        }).start();
        return "runnableThreadHelloWord";
    }


    /**
     * Spring threadPool
     *
     * @param str
     * @return
     */
    @PostMapping("/springThreadPool")
    public String springThreadPool(final String str) {
        threadPoolTaskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "springThreadPool";
    }

    /**
     * Spring Async
     *
     * @param str
     * @return
     */
    @Async("newTaskExecutor")
    @PostMapping("/springAsyncThreadPool")
    public String springAsyncThreadPool(final String str) {
        threadPoolTaskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "springAsyncThreadPool";
    }


    /**
     * completableFuture
     *
     * @param str
     * @return
     */
    @PostMapping("/completableFuture")
    public String completableFuture(final String str) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        }, threadPoolTaskExecutor);
        return "completableFuture";
    }

    /**
     * 嵌套线程池
     *
     * @param str
     * @return
     */
    @PostMapping("/nestedThreadPool")
    public String nestedThreadPool(final String str) {
        Executors.newFixedThreadPool(4).submit(new Runnable() {
            @Override
            public void run() {
                threadPoolTaskExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        springCloudBApi.helloWord(str);
                    }
                });
            }
        });
        return "nestedThreadPool";
    }


    private static ScheduledThreadPoolExecutor scheduledThreadPoolRunnableExecutor = new ScheduledThreadPoolExecutor(1);

    @PostMapping("/scheduledThreadPoolRunnableExecutor")
    public String scheduledThreadPoolRunnableExecutor(final String str) {
        scheduledThreadPoolRunnableExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        }, 1, TimeUnit.SECONDS);
        return "nestedThreadPool";
    }

    private static ScheduledThreadPoolExecutor scheduledThreadPoolCallableExecutor = new ScheduledThreadPoolExecutor(1);

    @PostMapping("/scheduledThreadPoolCallableExecutor")
    public String scheduledThreadPoolCallableExecutor(final String str) {
        scheduledThreadPoolCallableExecutor.schedule(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        }, 1, TimeUnit.SECONDS);
        return "nestedThreadPool";
    }


    @PostMapping("/threadPoolRunnableExecutor")
    public String threadPoolRunnableExecutor(final String str) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "threadPoolRunnableExecutor";
    }

    @PostMapping("/cachedThreadPoolRunnable")
    public String cachedThreadPoolRunnable(final String str) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "cachedThreadPoolRunnable";
    }


    @PostMapping("/cachedThreadPoolCallable")
    public String cachedThreadPoolCallable(final String str) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                springCloudBApi.helloWord(str);
                return null;
            }
        });
        return "cachedThreadPoolCallable";
    }

    @PostMapping("/scheduledThreadPoolRunnable")
    public String scheduledThreadPoolRunnable(final String str) {
        ExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "scheduledThreadPoolRunnable";
    }

    @PostMapping("/scheduledThreadPoolCallable")
    public String scheduledThreadPoolCallable(final String str) {
        ExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                springCloudBApi.helloWord(str);
                return null;
            }
        });
        return "scheduledThreadPoolCallable";
    }


    @PostMapping("/singleThreadPoolRunnable")
    public String singleThreadPoolRunnable(final String str) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }
        });
        return "singleThreadPoolRunnable";
    }

    @PostMapping("/singleThreadPoolCallable")
    public String singleThreadPoolCallable(final String str) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {

                springCloudBApi.helloWord(str);
                return null;
            }
        });
        return "singleThreadPoolCallable";
    }

    /**
     * fixedThreadPoolRunnable
     *
     * @param str
     * @return
     */
    @GetMapping("/fixedThreadPoolRunnable")
    public String fixedThreadPoolRunnable(final String str) {
        Executors.newFixedThreadPool(4).submit(new Runnable() {

            @Override
            public void run() {
                springCloudBApi.helloWord(str);
            }

        });
        return "fixedThreadPoolRunnable";
    }

    /**
     * fixedThreadPoolCallable
     *
     * @param str
     * @return
     */
    @GetMapping("/fixedThreadPoolCallable")
    public String fixedThreadPoolCallable(final String str) {
        Executors.newFixedThreadPool(4).submit(new Callable() {

            @Override
            public Object call() throws Exception {
                springCloudBApi.helloWord(str);
                return null;
            }

        });
        return "fixedThreadPoolCallable";
    }


    @PostMapping("/simpleLimiting")
    public String simpleLimiting(final String str) {
        return "limiting";
    }


    //    @SneakyThrows
    @PostMapping("/cpuLimiting")
    public String cpuLimiting(final String str) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        springCloudBApi.helloWord(str);
//                        Thread.sleep(200);
                    }
                }
            }).start();
        }
        return "cpuLimiting";
    }


}
