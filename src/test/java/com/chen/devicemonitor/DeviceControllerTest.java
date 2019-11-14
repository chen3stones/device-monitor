package com.chen.devicemonitor;


import com.chen.devicemonitor.controller.DeviceController;
import com.chen.devicemonitor.controller.UserController;
import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.service.ScanService;
import com.chen.devicemonitor.service.SendMessageService;
import com.chen.devicemonitor.vo.DeleteView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceMonitorApplication.class)
public class DeviceControllerTest {
    @Autowired
    DeviceController deviceController;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    ScanService scanService;
    @Autowired
    UserController userController;
    @Autowired
    SendMessageService sendMessageService;
    @Test
    public void delete(){
        DeleteView deleteView = new DeleteView();
        deleteView.setId(12);
        deviceController.deleteById(deleteView);
    }

    @Test
    public void update(){
        Device device = deviceMapper.getDeviceById(13);
        device.setDIP("223");
        deviceController.update(device);
    }

    @Test
    public void scan(){
        scanService.getWrongDevice();
    }
    @Test
    public void user(){
        //userController.getUsers();
    }
    @Test
    public void sendMessage(){
        sendMessageService.sendMessage();
    }
}
