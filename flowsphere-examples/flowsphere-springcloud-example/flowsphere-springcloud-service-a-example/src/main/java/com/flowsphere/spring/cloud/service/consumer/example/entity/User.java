package com.flowsphere.spring.cloud.service.consumer.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {

    private int id;

    private String username;

}
