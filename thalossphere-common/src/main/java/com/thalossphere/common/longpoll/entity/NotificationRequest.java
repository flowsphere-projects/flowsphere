package com.thalossphere.common.longpoll.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class NotificationRequest implements Serializable {

    private String applicationName;

    private String ip;

    private int port;

}
