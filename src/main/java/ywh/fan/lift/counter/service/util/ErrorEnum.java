package ywh.fan.lift.counter.service.util;

public enum ErrorEnum {

    DEVICE_EMPTY(999,"没有查到该设备"),
    ;

    private ErrorEnum(int errorCode, String errorMsg){
        code = errorCode;
        msg = errorMsg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
