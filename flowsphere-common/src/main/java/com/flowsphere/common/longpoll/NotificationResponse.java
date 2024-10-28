package com.flowsphere.common.longpoll;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotificationResponse implements Serializable {

    private String cmd;

    private String message;

}
