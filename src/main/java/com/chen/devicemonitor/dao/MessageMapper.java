package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageMapper {
    Message getMessage(@Param("date") String date,@Param("userId") int userId,@Param("deviceId") int deviceId,@Param("port") String port);
    void insertMessage(@Param("m") Message message) ;
    List<Message> getAllMessage();
}
