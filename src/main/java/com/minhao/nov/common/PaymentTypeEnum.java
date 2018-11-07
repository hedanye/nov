package com.minhao.nov.common;

public enum PaymentTypeEnum {
    ALIPAY(1,"在线支付");


    private final int code;
    private final String msg;


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    PaymentTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static PaymentTypeEnum codeOf(int code){
        for (PaymentTypeEnum p :values()) {
            if (p.getCode()==code){
                return p;
            }
        }
        throw new RuntimeException("没有找到对应的枚举");
    }



}
