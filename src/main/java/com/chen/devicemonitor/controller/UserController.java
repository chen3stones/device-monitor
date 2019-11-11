package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @GetMapping("/list")
    public String getUsers(Model model){
        List<User> users = userMapper.getAllUser();
        model.addAttribute("users",users);
        return "user";
    }
}
