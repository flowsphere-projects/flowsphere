package com.flowsphere.agent.core.interceptor.type;

import com.flowsphere.agent.core.interceptor.MethodInterceptor;

public interface ConstructorInterceptor extends MethodInterceptor {

    void onConstructor(Object obj, Object[] allArguments);

}
