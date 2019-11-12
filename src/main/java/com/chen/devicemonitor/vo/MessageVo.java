package com.chen.devicemonitor.vo;

import lombok.Data;

@Data
public class MessageVo {
    Integer id;
    String deviceName;
    String userName;
    String date;
    String msg;
    String port;
}
