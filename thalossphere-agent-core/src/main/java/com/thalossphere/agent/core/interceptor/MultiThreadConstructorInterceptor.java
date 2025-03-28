package com.thalossphere.agent.core.interceptor;

import com.thalossphere.agent.core.context.CustomContext;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.common.tag.context.TagContext;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

public class MultiThreadConstructorInterceptor {

    @RuntimeType
    public void constructorMethodIntercept(@This Object obj, @AllArguments Object[] allArguments) {
        CustomContextAccessor customContextAccessor = (CustomContextAccessor) obj;
        customContextAccessor.setCustomContext(new CustomContext(TagContext.get()));
    }

}
