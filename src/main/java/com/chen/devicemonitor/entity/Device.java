package com.chen.devicemonitor.entity;

import lombok.Data;

@Data
public class Device {
    Integer dId;
    String dName;
    String dIP;
    String dPort;
    Integer type;
}
