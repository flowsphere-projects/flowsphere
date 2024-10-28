package com.flowsphere.common.longpoll;

import com.flowsphere.common.config.YamlAgentConfig;
import com.flowsphere.common.config.YamlAgentConfigCache;
import com.flowsphere.common.transport.SimpleHttpClient;
import com.flowsphere.common.transport.SimpleHttpRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LongPollService {

    private static final LongPollService INSTANT = new LongPollService();

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    private static final String NOTIFICATION_API_URL = "/notification/pollNotification";

    public static LongPollService getInstance() {
        return INSTANT;
    }

    public void startLongPolling(String serverAddr, String applicationName, String ip) {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        SCHEDULER.schedule(new Runnable() {
            @Override
            public void run() {
                SimpleHttpClient.getInstance().send(new SimpleHttpRequest()
                        .setUrl(serverAddr + NOTIFICATION_API_URL)
                        .setData(
                                new NotificationRequest()
                                .setApplicationName(applicationName)
                                .setIp(ip)
                        )
                );
            }
        }, yamlAgentConfig.getLongPollDelay(), TimeUnit.SECONDS);
    }

}
