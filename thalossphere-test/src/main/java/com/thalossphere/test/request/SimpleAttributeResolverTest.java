package com.thalossphere.test.request;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.request.HttpRequest;
import com.thalossphere.common.request.SimpleAttributeResolver;
import com.thalossphere.common.request.SimpleRequestResolver;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SimpleAttributeResolverTest {

    @Test
    public void requestResolverTest() {
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(httpRequest.getParameters()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA"
        ));
        Mockito.when(httpRequest.getHeaders()).thenReturn(ImmutableMap.of(
                CommonConstant.REGION, "tagA"
        ));
        Mockito.when(httpRequest.getCookies()).thenReturn(ImmutableMap.of(
                CommonConstant.USER_ID, "tagA"
        ));
        SimpleRequestResolver simpleRequestResolver = new SimpleRequestResolver(httpRequest);
        SimpleAttributeResolver simpleAttributeResolver = new SimpleAttributeResolver(simpleRequestResolver);
        Assertions.assertTrue(simpleAttributeResolver.getTag().equals("tagA"));
        Assertions.assertTrue(simpleAttributeResolver.getRegion().equals("tagA"));
        Assertions.assertTrue(simpleAttributeResolver.getUserId().equals("tagA"));
    }

}
