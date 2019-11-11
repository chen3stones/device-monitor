package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.service.ScanService;
import com.chen.devicemonitor.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    SendMessageService sendMessageService;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ScanService scanService;
    @GetMapping("/ttt")
    public void test(){
        List<Device> list = scanService.getWrongDevice();
        for(Device device : list) {
            System.out.println(device);
        }
    }
}
