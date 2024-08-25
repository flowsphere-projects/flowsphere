package com.flowsphere.agent.core.enums;

public enum MethodTypeEnum {

    STATIC("static"),
    CONSTRUCTOR("constructor"),
    INSTANT("instant")
    ;

    private final String type;


    MethodTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
