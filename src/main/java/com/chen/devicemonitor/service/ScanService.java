package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@Service
public class ScanService {
    @Resource
    DeviceMapper deviceMapper;
    private static final Logger logger = LoggerFactory.getLogger(ScanService.class);
    public List<Device> getWrongDevice() {
        List<Device> devices = deviceMapper.getAllDevice();
        List<Device> wrongDevices = new ArrayList<Device>();
        for(Device device : devices) {
            if(checkDevice(device)) {
                if(StringUtil.isEmpty(device.getDPort())) {
                    if(!isOnline(device.getDIP(),0)){
                        wrongDevices.add(device);
                    }
                } else {
                    try {
                        String[] ports = device.getDPort().split(",");
                        for(String port : ports) {
                            System.out.println("test " + device.getDIP() + ":" + device.getDPort());
                            if(!isOnline(device.getDIP(),Integer.valueOf(port))){
                                wrongDevices.add(device);
                            }
                        }
                    } catch (Exception e){
                        logger.error("{} has wrong port",device.toString());
                    }
                }
            }
        }
        return wrongDevices;
    }
    private boolean isOnline(String hostName,int port) {
        if(port == 0) {
            return checkByPing(hostName);
        }else {
            return checkByTail(hostName,port);
        }
    }

    private boolean checkByPing(String hostName) {
        if(StringUtil.isEmpty(hostName)) {
            return false;
        }
        try {
            InetAddress inetAddress;
            boolean online = false;
            inetAddress = InetAddress.getByName(hostName);
            online = inetAddress.isReachable(1500);
            return online;
        } catch (IOException e) {
            logger.error("{} is outline with log:{}",hostName,e.getMessage());
        }
        return false;
    }

    private boolean checkByTail(String hostName,int port) {
        Socket socket =  null;
        try {
            socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(hostName,port);
            socket.connect(address,3000);
            return true;
        }catch (Exception e) {
            logger.error("{}:{} is outline with log:{}",hostName,port,e.getMessage());
        }
        return false;
    }
    private boolean checkDevice(Device device) {
        if(device == null || StringUtil.isEmpty(device.getDIP())) {
            return false;
        }
        return true;
    }
}
