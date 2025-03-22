package com.thalossphere.common.transport;

import com.thalossphere.common.config.OkHttpConfig;
import com.thalossphere.common.config.YamlAgentConfig;
import com.thalossphere.common.config.YamlAgentConfigCache;
import com.thalossphere.common.utils.JacksonUtils;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class SimpleHttpClient {

    private static final SimpleHttpClient INSTANT = new SimpleHttpClient();

    private static final MediaType MEDIA_TYPE = MediaType.get("application/json");

    private OkHttpClient okHttpClient;

    public SimpleHttpClient() {
        YamlAgentConfig yamlAgentConfig = YamlAgentConfigCache.get();
        OkHttpConfig okHttpConfig = yamlAgentConfig.getOkHttpConfig();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(okHttpConfig.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(okHttpConfig.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(okHttpConfig.getReadTimeout(), TimeUnit.SECONDS)
                .build();
    }

    public static SimpleHttpClient getInstance() {
        return INSTANT;
    }

    @SneakyThrows
    public Response send(SimpleHttpRequest simpleHttpRequest) {
        RequestBody body = RequestBody.create(MEDIA_TYPE, JacksonUtils.toJson(simpleHttpRequest.getData()));
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(simpleHttpRequest.getUrl())
                .post(body)
                .build();
        return okHttpClient.newCall(request).execute();
    }

}
