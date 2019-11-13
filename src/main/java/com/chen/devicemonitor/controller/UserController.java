package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.service.ExcelService;
import com.chen.devicemonitor.vo.DeleteView;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserMapper userMapper;
    @Autowired
    ExcelService excelService;
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
    public void addUser(@RequestBody User user,HttpServletResponse response) {
        userMapper.addUser(user);
    }

    @ResponseBody
    @PostMapping("/update")
    public void updateUser(@RequestBody User user) {
        userMapper.updateUser(user);
    }

    @GetMapping("/updateUser")
    public String updateUserPage(@RequestParam("id") Integer id, Model model){
        User user = userMapper.getUserById(id);
        model.addAttribute(user);
        return "userUpdate";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public void upload(HttpServletRequest request,HttpServletResponse response) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("filename");
        if(file.isEmpty()) {
            //return "文件不能为空";
        }
        InputStream inputStream = file.getInputStream();
        List<User> list = excelService.getUserFromExcel(inputStream,file.getOriginalFilename());
        for(User user : list) {
            try {
                userMapper.addUser(user);
                System.out.println(user.toString());
            } catch (Exception e) {
                logger.error("批量插入设备{}失败,log{}",user.toString(),e.getMessage());
                e.printStackTrace();
            }
        }
        response.sendRedirect("/user/list");
    }

    @ResponseBody
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("用户表.xls", "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        Workbook workbook = excelService.createUserExcelFile();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
