package com.flowsphere.agent.core.interceptor.template;

import lombok.Data;

@Data
public class InstantMethodInterceptorResult {

    private boolean isContinue = true;

    private Object result = null;

}
