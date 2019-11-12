package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.vo.DeleteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    @PostMapping("/delete")
    public void deleteUser(@RequestBody DeleteView deleteView) {
        userMapper.deleteUser(deleteView.getId());
    }

    @ResponseBody
    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        userMapper.addUser(user);
    }

    @ResponseBody
    @PostMapping("/update")
    public void updateUser(@RequestBody User user) {
        userMapper.updateUser(user);
    }
}
