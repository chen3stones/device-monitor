package com.chen.devicemonitor.en;

public enum  DeviceRoleEnum {
    NET_DEVICE(0,"网络设备"),
    SERVER_DEVICE(1,"服务器"),
    UNKNOWN_DEVICE(-1,"未知设备");
    int code;
    String desc;
    DeviceRoleEnum(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    public int getCode() {
        return code;
    }
    public static DeviceRoleEnum getByCode(int code) {
        switch (code) {
            case 0:
                return NET_DEVICE;
            case 1:
                return SERVER_DEVICE;
            default:
                return UNKNOWN_DEVICE;
        }
    }

    public static DeviceRoleEnum getByDesc(String desc) {
        switch (desc) {
            case "网络设备":
                return NET_DEVICE;
            case "服务器":
                return SERVER_DEVICE;
            default:
                return UNKNOWN_DEVICE;
        }
    }
}
