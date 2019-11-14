package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMapper {
    List<Device> getAllDevice();
    Device getDeviceById(Integer id);
    Device getDeviceByName(String name);
    Integer insertDevice(@Param("device") Device device);
    Integer updateDevice(@Param("device") Device device);
    Integer deleteDeviceById(Integer id);
    void clear();
}
