package com.chen.devicemonitor.dao;

import com.chen.devicemonitor.entity.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceMapper {
    List<Device> getAllDevice();
    Integer insertDevice(Device device);
    Integer updateDevice(Device device);
    Integer deleteDeviceById(Integer id);
}
