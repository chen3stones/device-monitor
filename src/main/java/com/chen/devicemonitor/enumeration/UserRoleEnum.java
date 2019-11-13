package com.chen.devicemonitor.enumeration;

public enum  UserRoleEnum {
    NET_ADMIN(0,"网络管理员"),
    SYSTEM_ADMIN(1,"系统管理员"),
    UNKNOWN_USER(-1,"未知管理员");
    int code;
    String desc;
    UserRoleEnum(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    public int getCode() {
        return code;
    }
    public static UserRoleEnum getByCode(int code) {
        switch (code) {
            case 0:
                return NET_ADMIN;
            case 1:
                return SYSTEM_ADMIN;
            default:
                return UNKNOWN_USER;
        }
    }

    public static UserRoleEnum getByDesc(String desc) {
        switch (desc) {
            case "网络管理员":
                return NET_ADMIN;
            case "系统管理员":
                return SYSTEM_ADMIN;
            default:
                return UNKNOWN_USER;
        }
    }
}
