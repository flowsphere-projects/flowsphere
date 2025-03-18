package com.flowsphere.spring.cloud.service.consumer.example.controller;

import com.flowsphere.common.tag.context.TagContext;
import com.flowsphere.common.tag.context.TagManager;
import com.flowsphere.common.utils.JacksonUtils;
import com.flowsphere.spring.cloud.service.api.SpringCloudBApi;
import com.flowsphere.spring.cloud.service.api.SpringCloudCApi;
import com.flowsphere.spring.cloud.service.api.entity.TagEntity;
import com.flowsphere.spring.cloud.service.consumer.example.entity.User;
import com.flowsphere.spring.cloud.service.consumer.example.mapper.UserMapper;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping()
public class SpringCloudServiceA1Controller {

    @Autowired
    private SpringCloudBApi springCloudBApi;

    @Autowired
    private SpringCloudCApi springCloudCApi;

    @Qualifier("taskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/findById")
    public User findById(int id){
        return userMapper.findById(id);
    }



    @SneakyThrows
    @GetMapping("/lazy")
    public String lazy() {
        Thread.sleep(300);
        return "lazy";
    }

    @PostMapping("/restTemplate")
    public List<TagEntity> restTemplate(String str) {
        String result = restTemplate.postForObject("http://service-b/service-b/helloWord?str=" + str, String.class, String.class);
        List<TagEntity> tagEntities = JacksonUtils.toList(result, TagEntity.class);
        tagEntities.add(TagEntity.build("SpringCloudProviderA"));
        return tagEntities;
    }

    @PostMapping("/helloWord")
    public List<TagEntity> helloWord(String str) {
        List<TagEntity> tagEntities = springCloudBApi.helloWord(str);
        tagEntities.add(TagEntity.build("SpringCloudProviderA1"));
        springCloudCApi.helloWord(str);
        return tagEntities;
    }

    @PostMapping("/repeat")
    public List<TagEntity> repeat(String str) {
        springCloudBApi.helloWord(str);
        List<TagEntity> tagEntities = springCloudBApi.repeat(str);
        tagEntities.add(TagEntity.build("SpringCloudProviderA1"));
        return tagEntities;
    }

    @PostMapping("/helloWordAsync")
    public String helloWordAsync(String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("获取到tag=" + TagContext.get());
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("helloWordAsync");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
            List<TagEntity> tagList = new ArrayList<>();
            TagEntity tag = new TagEntity();
            tag.setTag(TagContext.get());
            tag.setSystemTag(TagManager.getSystemTag());
            tag.setConsumer("runnableLambdaThread");
            tagList.add(tag);
            springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("runnableThread");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("springThreadPool");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("springAsyncThreadPool");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("completableFuture");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                        List<TagEntity> tagList = new ArrayList<>();
                        TagEntity tag = new TagEntity();
                        tag.setTag(TagContext.get());
                        tag.setSystemTag(TagManager.getSystemTag());
                        tag.setConsumer("nestedThreadPool");
                        tagList.add(tag);
                        springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("scheduledThreadPoolRunnableExecutor");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("scheduledThreadPoolCallableExecutor");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("threadPoolRunnableExecutor");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("cachedThreadPoolRunnable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("cachedThreadPoolCallable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("scheduledThreadPoolRunnable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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

                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("scheduledThreadPoolCallable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("singleThreadPoolRunnable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("singleThreadPoolCallable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
    @PostMapping("/fixedThreadPoolRunnable")
    public String fixedThreadPoolRunnable(final String str) {
        Executors.newFixedThreadPool(4).submit(new Runnable() {

            @Override
            public void run() {
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("fixedThreadPoolRunnable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
    @PostMapping("/fixedThreadPoolCallable")
    public String fixedThreadPoolCallable(final String str) {
        Executors.newFixedThreadPool(4).submit(new Callable() {

            @Override
            public Object call() throws Exception {
                List<TagEntity> tagList = new ArrayList<>();
                TagEntity tag = new TagEntity();
                tag.setTag(TagContext.get());
                tag.setSystemTag(TagManager.getSystemTag());
                tag.setConsumer("fixedThreadPoolCallable");
                tagList.add(tag);
                springCloudBApi.tagSpread(tagList);
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
