package com.flowsphere.test.request;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.request.HttpRequest;
import com.flowsphere.common.request.SimpleRequestResolver;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SimpleRequestResolverTest {

    @Test
    public void requestResolverTest() {
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(httpRequest.getParameters()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA"
        ));
        Mockito.when(httpRequest.getHeaders()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA"
        ));
        Mockito.when(httpRequest.getCookies()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA"
        ));
        SimpleRequestResolver simpleRequestResolver = new SimpleRequestResolver(httpRequest);
        Assertions.assertTrue(simpleRequestResolver.getParameters(CommonConstant.TAG).equals("tagA"));
        Assertions.assertTrue(simpleRequestResolver.getHeader(CommonConstant.TAG).equals("tagA"));
        Assertions.assertTrue(simpleRequestResolver.getCookie(CommonConstant.TAG).equals("tagA"));
    }

}
