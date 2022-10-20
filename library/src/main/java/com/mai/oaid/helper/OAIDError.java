package com.mai.oaid.helper;

/**
 * OAID获取失败的错误枚举
 */
public enum OAIDError {

    REQUESTING(1001, "初始化中"),
    NOT_SUPPORT(1002, "不支持的设备"),
    SERVICE_ERROR(1003, "获取失败"),
    LIMITED(1003, "用户设置为限制获取"),
    STATUS_ERROR(1004, "读取错误"),
    RETURN_EMPTY(1005, "返回为空"),
    UNKNOWN_ERROR(1006, "未知错误");

    public int errCode;
    public String errMsg;

    OAIDError(int code, String msg) {
        this.errCode = code;
        this.errMsg = msg;
    }

}
