package com.chen.devicemonitor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Device {
    @JsonProperty("dId")
    Integer dId;
    @JsonProperty("dName")
    String dName;
    @JsonProperty("dIP")
    String dIP;
    @JsonProperty("dPort")
    String dPort;
    @JsonProperty("type")
    Integer type;
}
