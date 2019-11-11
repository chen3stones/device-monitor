package com.chen.devicemonitor.entity;

import lombok.Data;

@Data
public class Message {
    Integer mId;
    Integer dId;
    Integer uId;
    String date;
    String msg;
}
