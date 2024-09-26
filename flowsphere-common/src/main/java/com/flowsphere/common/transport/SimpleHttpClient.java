package com.flowsphere.common.transport;

import com.flowsphere.common.utils.JacksonUtils;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class SimpleHttpClient  {

    private static final SimpleHttpClient INSTANT = new SimpleHttpClient();

    public static SimpleHttpClient getInstance() {
        return INSTANT;
    }

    private OkHttpClient okHttpClient;

    private static final MediaType MEDIA_TYPE = MediaType.get("application/json");

    public SimpleHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @SneakyThrows
    public void send(SimpleHttpRequest simpleHttpRequest) {
        RequestBody body = RequestBody.create(MEDIA_TYPE, JacksonUtils.toJson(simpleHttpRequest.getData()));
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(simpleHttpRequest.getUrl())
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("okhttp post execute error");
            }
        }
    }

}
