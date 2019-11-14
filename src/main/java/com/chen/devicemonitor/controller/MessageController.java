package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Device;
import com.chen.devicemonitor.entity.Message;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.enumeration.DeviceRoleEnum;
import com.chen.devicemonitor.enumeration.UserRoleEnum;
import com.chen.devicemonitor.service.SendMessageService;
import com.chen.devicemonitor.service.WebSocketService;
import com.chen.devicemonitor.vo.MessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/message")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SendMessageService sendMessageService;
    @GetMapping("/list/{type}")
    public String getAllMessage(Model model, @PathVariable("type") String type,
                                @RequestParam(required = false,defaultValue = "1")Integer page,
                                @RequestParam(required = false,defaultValue = "") String deviceName,
                                @RequestParam(required = false,defaultValue = "") String userName) {
        Message filterMessage = new Message();
        List<MessageVo> messageVos = new ArrayList<>();
        List<Message> messages = messageMapper.getAllMessage();
        messages = messages.stream().sorted(Comparator.comparingInt(Message::getStatus)).collect(Collectors.toList());
        for(Message message : messages) {
            try {
                MessageVo messageVo = translate(message);
                messageVos.add(messageVo);
            }catch (Exception e) {
                logger.error("{} is wrong to translate into messageVo,log is {}",message.toString(),e.getMessage());
            }
        }
        model.addAttribute("type",type);
        model.addAttribute("messages",messageVos);
        List<User> users = userMapper.getAllUser();
        List<Device> devices = deviceMapper.getAllDevice();
        model.addAttribute("users",users);
        model.addAttribute("devices",devices);
        return "message";
    }

    @ResponseBody
    @RequestMapping("/push")
    public void pushMessage() throws IOException {
        sendMessageService.sendMessage();
    }

    @ResponseBody
    @GetMapping("/confirm/{id}")
    public void quality(@PathVariable("id")Integer id){
        Message message = messageMapper.getMessageById(id);
        message.setStatus(1);
        messageMapper.updateMessage(message);
    }
    private MessageVo translate(Message message) {
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getMId());
        messageVo.setDeviceName(deviceMapper.getDeviceById(message.getDId()).getDName());
        messageVo.setUserName(userMapper.getUserById(message.getUId()).getUName());
        messageVo.setDate(message.getDate());
        messageVo.setMsg(message.getMsg());
        messageVo.setPort(message.getDPort());
        messageVo.setStatus(message.getStatus());
        return messageVo;
    }
}
