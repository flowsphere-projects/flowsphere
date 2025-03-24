package com.thalossphere.test.request;

import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.request.*;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ParameterResolverTest {

    @Test
    public void parameterResolverTest(){
        HttpRequest httpRequest = Mockito.mock(HttpRequest.class);
        Mockito.when(httpRequest.getParameters()).thenReturn(ImmutableMap.of(
                CommonConstant.TAG, "tagA",
                CommonConstant.USER_ID, "123",
                CommonConstant.REGION, "111"
        ));

        RequestResolver requestResolver = new SimpleRequestResolver(httpRequest);
        ParameterResolver parameterResolver = new ParameterResolver() {
        };
        Assertions.assertTrue(parameterResolver.getUserId(requestResolver).equals("123"));
        Assertions.assertTrue(parameterResolver.getRegion(requestResolver).equals("111"));
        Assertions.assertTrue(parameterResolver.getTag(requestResolver).equals("tagA"));
    }

}
