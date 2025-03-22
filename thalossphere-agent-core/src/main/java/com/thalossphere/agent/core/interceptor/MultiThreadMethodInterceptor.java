package com.thalossphere.agent.core.interceptor;

import com.thalossphere.agent.core.context.CustomContext;
import com.thalossphere.agent.core.context.CustomContextAccessor;
import com.thalossphere.common.tag.context.TagContext;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class MultiThreadMethodInterceptor {

    @RuntimeType
    public Object instantMethodIntercept(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> callable, @Origin Method method) throws Exception {
        try {
            CustomContextAccessor customContextAccessor = (CustomContextAccessor) obj;
            Object object = customContextAccessor.getCustomContext();
            if (Objects.nonNull(object) && object instanceof CustomContext) {
                CustomContext customContext = (CustomContext) object;
                TagContext.set(customContext.getTag());
            }
            Object result = callable.call();
            return result;
        } finally {
            TagContext.remove();
        }
    }

}
