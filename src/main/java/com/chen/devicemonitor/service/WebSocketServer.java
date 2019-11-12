package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{uid}")
@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServerSet = new CopyOnWriteArraySet<>();
    private Session session;
    private Integer uId;

    @Autowired
    UserMapper userMapper;
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") Integer uId){
        this.session = session;
        webSocketServerSet.add(this);
        this.uId = uId;
        try{
            User user = userMapper.getUserById(uId);
            if(user != null) {
                sendMessage("尊敬的{},欢迎您");
            }
        }catch (Exception e) {
            logger.error("{} error to connect",uId);
        }
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
