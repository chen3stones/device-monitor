package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Repository
public interface MessageMapper {
    Message getMessage(@Param("date") String date,@Param("userId") int userId,@Param("deviceId") int deviceId);
    void insertMessage(@Param("m") Message message) ;
}
