package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.service.ExcelService;
import com.chen.devicemonitor.vo.DeleteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    ExcelService excelService;
    @GetMapping("/list")
    public String Devices(Model model){
        List<Device> devices = deviceMapper.getAllDevice();
        model.addAttribute("devices",devices);
        return "devices";
    }
    @ResponseBody
    @PostMapping("/delete")
    public void deleteById(@RequestBody DeleteView deleteView) {
        deviceMapper.deleteDeviceById(deleteView.getId());
    }
    @ResponseBody
    @PostMapping("/add")
    public void insert(@RequestBody Device device) {
        deviceMapper.insertDevice(device);
    }
    @ResponseBody
    @PostMapping("/update")
    public void update(@RequestBody Device device){
        deviceMapper.updateDevice(device);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public void upload(HttpServletRequest request) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("filename");
        if(file.isEmpty()) {
            //return "文件不能为空";
        }
        InputStream inputStream = file.getInputStream();
        List<Device> list = excelService.getDeviceByExcel(inputStream,file.getOriginalFilename());
        for(Device device : list) {
            try {
                System.out.println(device.toString());
                //deviceMapper.insertDevice(device);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
