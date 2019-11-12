package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> getAllUser();
    User getUserById(Integer id);
    Integer addUser(@Param("user") User user);
    Integer updateUser(@Param("user") User user);
    Integer deleteUser(Integer id);
}
