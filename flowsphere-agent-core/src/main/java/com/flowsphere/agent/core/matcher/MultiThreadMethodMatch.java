package com.flowsphere.agent.core.matcher;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;

public class MultiThreadMethodMatch<T> extends ElementMatcher.Junction.AbstractBase<T> {

    private static final String RUNNABLE_METHOD_NAME = "run";

    private static final String CALLABLE_METHOD_NAME = "call";

    @Override
    public boolean matches(T target) {
        NamedElement namedElement = (NamedElement) target;
        if (namedElement.getActualName().equals(RUNNABLE_METHOD_NAME) || namedElement.getActualName().equals(CALLABLE_METHOD_NAME)) {
            return true;
        }
        return false;
    }

}
