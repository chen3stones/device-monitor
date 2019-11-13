package com.chen.devicemonitor.controller;

import com.chen.devicemonitor.dao.DeviceMapper;
import com.chen.devicemonitor.dao.MessageMapper;
import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.Message;
import com.chen.devicemonitor.service.WebSocketService;
import com.chen.devicemonitor.vo.MessageVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    WebSocketService webSocketService;
    @GetMapping("/list")
    public String getAllMessage(Model model) {
        List<MessageVo> messageVos = new ArrayList<>();
        List<Message> messages = messageMapper.getAllMessage();
        for(Message message : messages) {
            try {
                MessageVo messageVo = translate(message);
                messageVos.add(messageVo);
            }catch (Exception e) {
                logger.error("{} is wrong to translate into messageVo,log is {}",message.toString(),e.getMessage());
            }
        }
        model.addAttribute("messages",messageVos);
        return "message";
    }

    @ResponseBody
    @RequestMapping("/push")
    public void pushMessage() throws IOException {
        webSocketService.sendMessage("message form server");
    }

    private MessageVo translate(Message message) {
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getMId());
        messageVo.setDeviceName(deviceMapper.getDeviceById(message.getDId()).getDName());
        messageVo.setUserName(userMapper.getUserById(message.getUId()).getUName());
        messageVo.setDate(message.getDate());
        messageVo.setMsg(message.getMsg());
        messageVo.setPort(message.getDPort());
        return messageVo;
    }
}
