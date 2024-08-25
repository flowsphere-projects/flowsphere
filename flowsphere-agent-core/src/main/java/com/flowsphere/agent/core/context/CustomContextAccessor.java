package com.flowsphere.agent.core.context;

public interface CustomContextAccessor {

    Object getCustomContext();

    void setCustomContext(Object value);
}
