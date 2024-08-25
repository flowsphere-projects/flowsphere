package com.flowsphere.agent.core.utils;

import net.bytebuddy.description.method.MethodDescription;

public class MethodTypeUtils {

    public static boolean isConstructorMethod(final MethodDescription.InDefinedShape methodDescription) {
        return methodDescription.isConstructor();
    }

    public static boolean isStaticMethod(final MethodDescription.InDefinedShape methodDescription) {
        return methodDescription.isStatic() && isInstantMethod(methodDescription);
    }

    public static boolean isInstantMethod(final MethodDescription.InDefinedShape methodDescription) {
        return !(methodDescription.isAbstract() || methodDescription.isSynthetic());
    }

}
