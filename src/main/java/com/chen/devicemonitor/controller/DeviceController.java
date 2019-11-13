package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.service.ExcelService;
import com.chen.devicemonitor.vo.DeleteView;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {
    Logger logger = LoggerFactory.getLogger(DeviceController.class);
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
    public void upload(HttpServletRequest request,HttpServletResponse response) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("filename");
        if(file == null || file.isEmpty()) {
            return;
        }
        InputStream inputStream = file.getInputStream();
        List<Device> list = excelService.getDeviceFromExcel(inputStream,file.getOriginalFilename());
        for(Device device : list) {
            try {
                deviceMapper.insertDevice(device);
            } catch (Exception e) {
                logger.error("批量插入设备{}失败,log{}",device.toString(),e.getMessage());
                e.printStackTrace();
            }
        }
        response.sendRedirect("/device/list");
    }
    @GetMapping("/updatePage")
    public String updateDevicePage(@RequestParam("id") Integer id,Model model) {
        Device device = deviceMapper.getDeviceById(id);
        model.addAttribute("device",device);
        return "deviceUpdate";
    }

    @ResponseBody
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("设备表.xls", "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        Workbook workbook = excelService.createDeviceExcelFile();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
}
