package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{uid}")
@Component
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    private static CopyOnWriteArraySet<WebSocketService> webSocketServiceSet = new CopyOnWriteArraySet<>();
    private static Session session;
    private Integer uId;

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") Integer uId){
        this.session = session;
        webSocketServiceSet.add(this);
        this.uId = uId;
        try{
            sendMessage("欢迎您");
        }catch (Exception e) {
            logger.error("there is no message for {}",uId);
        }
    }

    @OnClose
    public void onClose(){
        webSocketServiceSet.remove(this);
        logger.info("{} out of connect");
    }

    @OnMessage
    public void onMessage(String message,Session session){
        logger.info("receive message:{} from {}",message,uId);
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.error("error,{}",error.getMessage());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
