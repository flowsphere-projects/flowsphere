package com.thalossphere.agent.core.interceptor.type;

import com.thalossphere.agent.core.interceptor.MethodInterceptor;

public interface ConstructorInterceptor extends MethodInterceptor {

    void onConstructor(Object obj, Object[] allArguments);

}
