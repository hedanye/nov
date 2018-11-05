package com.minhao.nov.common;

public enum ProductStatusEnum {

    ON_SALE(1,"在线");





    private final int code;
    private final String msg;


    ProductStatusEnum(int code, String msg) {
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
