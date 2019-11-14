package com.chen.devicemonitor.service;

import com.chen.devicemonitor.dao.UserMapper;
import com.chen.devicemonitor.entity.User;
import com.chen.devicemonitor.enumeration.DeviceRoleEnum;
import com.chen.devicemonitor.enumeration.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{type}")
@Component
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
    private static CopyOnWriteArraySet<Session> netSessionSet = new CopyOnWriteArraySet<>();
    private static CopyOnWriteArraySet<Session> adminSessionSet = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("type") String type){
        logger.info(type);
        if(UserRoleEnum.NET_ADMIN.getEn_desc().equals(type)) {
            netSessionSet.add(session);
        }else{
            adminSessionSet.add(session);
        }
    }

    @OnClose
    public void onClose(Session session){
        if(netSessionSet.contains(session)){
            netSessionSet.remove(session);
        }else if(adminSessionSet.contains(session)){
            adminSessionSet.remove(session);
        }
        logger.info("{} out of connect");
    }

    @OnMessage
    public void onMessage(String message,Session session){
        logger.info("receive message:{} from {}",message);
    }

    @OnError
    public void onError(Session session,Throwable error){
        logger.error("error,{}",error.getMessage());
    }

    public void sendMessage(String message, DeviceRoleEnum device) {
        if(device == DeviceRoleEnum.NET_DEVICE) {
            for(Session session : netSessionSet){
                try {
                    session.getBasicRemote().sendText(message);
                }catch (IOException e){
                    logger.error("error send message {}",message);
                }

            }
        }else if(device == DeviceRoleEnum.SERVER_DEVICE){
            for(Session session : adminSessionSet){
                try {
                    session.getBasicRemote().sendText(message);
                }catch (IOException e){
                    logger.error("error send message {}",message);
                }

            }
        }
    }
}
