package com.chen.devicemonitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chen.devicemonitor.dao")
public class DeviceMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceMonitorApplication.class, args);
    }

}
