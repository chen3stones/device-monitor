package com.chen.devicemonitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("uId")
    Integer uId;
    @JsonProperty("uName")
    String uName;
    @JsonProperty("phone")
    String phone;
    @JsonProperty("type")
    Integer type;
}
