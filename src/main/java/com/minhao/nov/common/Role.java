package com.minhao.nov.common;

public enum Role {

    ROLE_CUSTOMER(0,"普通用户"),
    ROLE_ADMIN(1,"管理员");


    private final int code;
    private final String msg;

    Role(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
