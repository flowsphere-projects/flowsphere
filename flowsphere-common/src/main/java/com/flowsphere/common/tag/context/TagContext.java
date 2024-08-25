package com.flowsphere.common.tag.context;

public class TagContext {

    private static final ThreadLocal<String> RULE_LOCAL = new ThreadLocal<String>();

    public static void set(String tag) {
        RULE_LOCAL.set(tag);
    }

    public static void remove() {
        if (RULE_LOCAL.get() != null) {
            RULE_LOCAL.remove();
        }
    }

    public static String get() {
        return RULE_LOCAL.get();
    }

}
