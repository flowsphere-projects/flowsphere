package com.thalossphere.test.request;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.request.CookieResolver;
import com.thalossphere.common.request.HttpRequest;
import com.thalossphere.common.request.RequestResolver;
import com.thalossphere.common.request.SimpleRequestResolver;
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
