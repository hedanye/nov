package com.minhao.nov.common;

public enum OrderStatusEnum {
    NO_PAY(0,"未付款");


    private final int code;
    private final String msg;


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    OrderStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static OrderStatusEnum codeOf(int code){
        for (OrderStatusEnum o :values()) {
            if (o.getCode()==code){
                return o;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }
}
