package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.entity.Message;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.enumeration.DeviceRoleEnum;
import com.chen.devicemonitor.util.DateUtil;
import com.chen.devicemonitor.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class SendMessageService {
    Logger logger = LoggerFactory.getLogger(SendMessageService.class);
    @Resource
    ScanService scanService;
    @Resource
    UserMapper userMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    WebSocketService webSocketService;
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void sendMessage(){
        logger.info("{}, check and ready to send message", DateUtil.getNumberTime(System.currentTimeMillis()));
        List<Device> wrongDevices = scanService.getWrongDevice();
        List<User> users = userMapper.getAllUser();
        List<User> adminUsers = users.stream().filter(m->m.getType() == 1).collect(Collectors.toList());
        List<User> netUsers = users.stream().filter(m->m.getType() == 0).collect(Collectors.toList());

        for(Device device : wrongDevices) {
            //网络设备
            if(device.getType() == 0) {
                send(device,netUsers);
            }else{
                //服务器设备
                send(device,adminUsers);
            }
        }
    }
    private void send(Device device,List<User> users) {
        String date = DateUtil.getYMD(System.currentTimeMillis());
        for(User user : users) {
            Message message = messageMapper.getMessage(date,user.getUId(),device.getDId(),device.getDPort());
            if(message == null) {
                //发送消息，短信平台接入
                //写数据库模拟短信发送
                Message msg = new Message();
                msg.setDId(device.getDId());
                msg.setUId(user.getUId());
                msg.setDate(date);
                msg.setMsg(genMessage(device));
                msg.setDPort(device.getDPort());
                msg.setStatus(0);
                messageMapper.insertMessage(msg);
                //通知
                try {
                    webSocketService.sendMessage(msg.getMsg(), DeviceRoleEnum.getByCode(device.getType()));
                } catch (Exception e){
                    logger.error("send message to client fail,reason:{}",e.getMessage());
                }
            }
        }
    }

    private String genMessage(Device device) {
        String s =  DateUtil.getNumberTime(System.currentTimeMillis()) + "，" + device.getDName() + "-" + device.getDIP();
        if(StringUtil.isEmpty(device.getDPort())) {
            s += "，网络不通";
        }else{
            s = s + "，端口" + device.getDPort() + "不通";
        }
        s += "，请及时检查";
        return s;
    }
}
