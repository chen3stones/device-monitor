package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.entity.Message;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Scheduled(cron = "0 0/1 * * * ? ")
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        long now = System.currentTimeMillis();
        String date = simpleDateFormat.format(now);
        for(User user : users) {
            Message message = messageMapper.getMessage(date,user.getUId(),device.getDId());
            if(message == null) {
                //发送消息，短信平台接入
                //写数据库模拟短信发送
                Message msg = new Message();
                msg.setDId(device.getDId());
                msg.setUId(user.getUId());
                msg.setDate(date);
                msg.setMsg(genMessage(user,device));
                System.out.println(msg.toString());
                messageMapper.insertMessage(msg);
            }
        }
    }

    private String genMessage(User user,Device device) {
        return "尊敬的" + user.getUName() + ",设备" + device.getDName() + ":" + device.getDPort() + "网络中断，请查看";
    }
}
