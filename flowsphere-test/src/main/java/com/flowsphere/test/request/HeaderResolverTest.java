package com.flowsphere.test.request;

import com.flowsphere.common.constant.CommonConstant;
import com.flowsphere.common.request.*;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HeaderResolverTest {

    @Test
    public void headerResolverTest(){
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(httpRequest.getHeaders()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA",
                CommonConstant.USER_ID, "123",
                CommonConstant.REGION, "111"
        ));

        RequestResolver requestResolver = new SimpleRequestResolver(httpRequest);
        HeaderResolver headerResolver = new HeaderResolver() {
        };
        Assertions.assertTrue(headerResolver.getUserId(requestResolver).equals("123"));
        Assertions.assertTrue(headerResolver.getRegion(requestResolver).equals("111"));
        Assertions.assertTrue(headerResolver.getTag(requestResolver).equals("tagA"));
    }

}
