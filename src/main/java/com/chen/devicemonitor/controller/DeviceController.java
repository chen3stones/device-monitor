package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/message")
public class DeviceController {
    @Autowired
    DeviceMapper deviceMapper;
    @GetMapping("/list")
    public String messages(Model model){
        List<Device> devices = deviceMapper.getAllDevice();
        model.addAttribute("devices",devices);
        return "devices";
    }
}
