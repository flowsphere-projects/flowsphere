package com.flowsphere.agent.core.context;


import lombok.Getter;

@Getter
public class CustomContext {

    private String tag;

    private Thread originThread;

    public CustomContext(String tag) {
        this.tag = tag;
        this.originThread = Thread.currentThread();
    }


}
