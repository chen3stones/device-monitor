package com.chen.devicemonitor;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.event.DocumentEvent;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeviceMonitorApplication.class)
public class DeviceMapperTest {
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;

    @Test
    public void getAllDevice(){
        for(Device device : deviceMapper.getAllDevice()) {
            System.out.println(device.toString());
        }
    }
    @Test
    public void getDeviceById(){
        Device device = deviceMapper.getDeviceById(11);
        System.out.println(device.toString());
    }
    @Test
    public void getDeviceByName(){
        Device device = deviceMapper.getDeviceByName("百度11111");
    }
    @Test
    public void insertDevice(){
        Device device = new Device();
        device.setDName("test");
        device.setDIP("0000");
        device.setDPort("");
        device.setType(1);
        deviceMapper.insertDevice(device);
    }
    @Test
    public void updateDevice(){
        Device device = deviceMapper.getDeviceById(11);
        device.setDIP("123123");
        deviceMapper.updateDevice(device);
    }
    @Test
    public void deleteDeviceById(){
        deviceMapper.deleteDeviceById(12);
    }
}
