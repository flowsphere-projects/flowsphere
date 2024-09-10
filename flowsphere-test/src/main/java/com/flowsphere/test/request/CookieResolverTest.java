package com.flowsphere.test.request;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.request.CookieResolver;
import com.flowsphere.common.request.HttpRequest;
import com.flowsphere.common.request.RequestResolver;
import com.flowsphere.common.request.SimpleRequestResolver;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CookieResolverTest {

    @Test
    public void resolverTest() {
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(httpRequest.getCookies()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA",
                CommonConstant.USER_ID, "123",
                CommonConstant.REGION, "111"
        ));

        RequestResolver requestResolver = new SimpleRequestResolver(httpRequest);
        CookieResolver cookieResolver = new CookieResolver() {
        };
        Assertions.assertTrue(cookieResolver.getUserId(requestResolver).equals("123"));
        Assertions.assertTrue(cookieResolver.getRegion(requestResolver).equals("111"));
        Assertions.assertTrue(cookieResolver.getTag(requestResolver).equals("tagA"));
    }

}
