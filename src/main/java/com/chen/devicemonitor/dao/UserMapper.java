package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> getAllUser();
    Integer addUser(User user);
    Integer updateUser(User user);
    Integer deleteUser(Integer id);
}
