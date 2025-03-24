package com.thalossphere.agent.core.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MethodInterceptorManager {

    private static final Map<String, MethodInterceptor> METHOD_INTERCEPTOR_MAP = new HashMap<>();

    public static MethodInterceptor getMethodInterceptor(String methodInterceptorClassName, ClassLoader classLoader) {
        return METHOD_INTERCEPTOR_MAP.computeIfAbsent(methodInterceptorClassName, key -> createMethodInterceptor(methodInterceptorClassName, classLoader));
    }

    private static MethodInterceptor createMethodInterceptor(String methodInterceptorClassName, ClassLoader classLoader) {
        try {
            return (MethodInterceptor) Class.forName(methodInterceptorClassName, true, classLoader).getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
