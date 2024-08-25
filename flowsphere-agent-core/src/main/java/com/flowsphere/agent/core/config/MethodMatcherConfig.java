package com.flowsphere.agent.core.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

@Getter
@AllArgsConstructor
public class MethodMatcherConfig {

    private final ElementMatcher<? super MethodDescription> pointcut;

    private final String interceptorName;

    private final String type;


}
